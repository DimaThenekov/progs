package managers;

// import src.converters.SerializationManager;
// import src.interfaces.CommandManagerCustom;
// import src.loggerUtils.LoggerManager;
// import src.network.requests.Request;

import managers.ReceivingManager;
import managers.SendingManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TCPServer {
	private static final Logger LOGGER = LoggerFactory.getLogger("managers.TCPServer");
	public interface TCPExecute { public Object Execute(String s, Object o); }
    private int port;
    private HashSet<SocketChannel> sessions;
    private ReceivingManager receivingManager = new ReceivingManager();
    private SendingManager sendingManager = new SendingManager();
    private Selector selector;
    private TCPExecute executer;

    public TCPServer(int port, TCPExecute obj) {
        this.port = port;
		executer = obj;
        this.sessions = new HashSet<>();
    }

    public HashSet<SocketChannel> getSessions() {
        return sessions;
    }

    public void close() {
        for (var se: sessions) {
			try {
				se.close();
			} catch (Exception e) {}
        }
    }
    public void start() {
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            var socketAddress = new InetSocketAddress("localhost", port);
            serverSocketChannel.bind(socketAddress, port);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            LOGGER.info("Server started on :"+port+"...");
            while (true) {
                // blocking, wait for events
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) continue;
                    if (key.isAcceptable()) {accept(key);
                    }else if (key.isReadable()) {
                        var result = receivingManager.read(key);
                        if(result == null)
                            continue;
                        //Request request;
                        // object is done being transferred
                        var res = result;
                        int p = -1;
                        if (res == null)
                            continue;
                        for (int i = res.length - 1; i > -1; i--) {
                            if (res[i] != 0) {
                                p = i;
                                break;
                            }
                        }
                        var cutres = Arrays.copyOfRange(res, 0, p+1);
						//System.out.println(new String(result));
						
						try(var command = new ObjectInputStream(new ByteArrayInputStream(cutres))){
							var ret = executer.Execute(command.readUTF(), command.readObject());
							try(var baos = new ByteArrayOutputStream();
							var a=new ObjectOutputStream(baos)) {
								a.writeObject(ret);
								sendingManager.send((SocketChannel) key.channel(), baos.toByteArray());
							}
						} catch (Exception e) {
							LOGGER.error(e.getMessage());
							sendingManager.send((SocketChannel) key.channel(), "503".getBytes());
						}
					}
				}
			}
		} catch (IOException e) {
			if (e.getMessage().equals("Address already in use: bind")) {
				LOGGER.error("ddress already in use");
				port = (port+1)%32767;
				start();
			} else
				LOGGER.error("----------"+e.getMessage()+"------------------");
		}
	}




	private void accept(SelectionKey key) {
		try {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
			SocketChannel channel = serverSocketChannel.accept();
			LOGGER.info("socket connection accepted:" + channel.socket().getRemoteSocketAddress().toString());
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
			sessions.add(channel);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
