package managers;

// import src.converters.SerializationManager;
// import src.interfaces.CommandManagerCustom;
// import src.loggerUtils.LoggerManager;
// import src.network.requests.Request;

import managers.ReceivingManager;
import managers.SendingManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class TCPServer {
	private static final Logger LOGGER = LoggerFactory.getLogger("managers.TCPServer");
	public interface TCPExecute { public Object Execute(String s, Object o, String login, String pass); }
	private int port;
	private HashSet<SocketChannel> sessions;
	private ReceivingManager receivingManager = new ReceivingManager();
	private SendingManager sendingManager = new SendingManager();
	private Selector selector;
	private TCPExecute executer;
	private ForkJoinPool forkJoinPool = new ForkJoinPool();

	public class executerClass extends RecursiveTask<Object> {
		private final String cmd; private final Object obj; private final String log; private final String pas; private TCPExecute exe;
		public executerClass(String cmd, Object obj, String log, String pas, TCPExecute exe) { this.cmd = cmd; this.obj = obj; this.log = log; this.pas = pas; this.exe = exe; }
		@Override
		protected Object compute() { return exe.Execute(cmd, obj, log, pas); }
	}

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
					if (key.isAcceptable()) {accept(key);}
					else if (key.isReadable()) {processingKey(key);}
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

	private void processingKey(SelectionKey key) {
		receivingManager.read(key, (ObjectInputStream command)->{
			try {
				var cmd = command.readUTF();
				var log = command.readUTF();
				var pas = command.readUTF();
				var obj = command.readObject();
				var ret = forkJoinPool.invoke(new executerClass(cmd, obj, log, pas, executer));
				sendingManager.send((SocketChannel) key.channel(), (Object) ret);
			} catch (Exception e) {
				System.out.println(e);
				LOGGER.error(e.getMessage());
				sendingManager.send((SocketChannel) key.channel(), (Object) "503");
			}
		});
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
