package managers;

import managers.ClientReceivingManager;
import managers.ClientSendingManager;

// import org.slf4j.Logger;
// import src.loggerUtils.LoggerManager;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;



public class TCPClient {
    private InetSocketAddress host;
    private SocketChannel socketChannel;
	private ClientReceivingManager receivingManager = null;
	private ClientSendingManager sendingManager = null;
    // private Logger logger;

    public TCPClient(String host, int port) {
        this.host = new InetSocketAddress(host, port);
        // this.logger = LoggerManager.getLogger(TCPClient.class);
    }

    public SocketChannel start(String adr, int port) {
		host = new InetSocketAddress(adr, port);
		return start();
	}

    public SocketChannel start(String adr) {
		host = new InetSocketAddress(adr, host.getPort());
		return start();
	}

    public SocketChannel start() {
        for(;;){
            try {
                if(socketChannel != null)
                    socketChannel.close();
                this.socketChannel = SocketChannel.open();
                socketChannel.bind(new InetSocketAddress("127.0.0.1", 20000+(int)(Math.random()*10000)));
                socketChannel.configureBlocking(false);
                socketChannel.connect(host);
				receivingManager = new ClientReceivingManager(this);
				sendingManager = new ClientSendingManager(this);
                return socketChannel;
            } catch (Exception e) {
                try{
					System.out.print("TCP client: " + e.getMessage());
                    // logger.info("TCP server: " + e.getMessage());
                    socketChannel.close();
                    Thread.sleep(3000);
                }
                catch (Exception e2){
					System.out.print("TCP client: " + e2.getMessage());
                    // logger.info("TCP server: " + e2.getMessage());
                }
            }
        }
    }

    public void close() {
		try {
			socketChannel.finishConnect();
		} catch (Exception e) {}
	}

	public SocketChannel getSocketChannel () {
		return this.socketChannel;
	}

	public Object send (String s, Object obj) {
		try (var baos = new ByteArrayOutputStream(); var a=new ObjectOutputStream(baos)) {
			a.writeUTF(s);
			a.writeObject(obj);
			if (!sendingManager.send(baos.toByteArray()))
				return null;
			System.out.println("");
			try(var command = new ObjectInputStream(new ByteArrayInputStream(receivingManager.receive()))) {
				return command.readObject();
			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean isConnected() {
		return socketChannel != null && socketChannel.socket().isBound() && !socketChannel.socket().isClosed() && socketChannel.isConnected()
			&& !socketChannel.socket().isInputShutdown() && !socketChannel.socket().isOutputShutdown();
	}
}
