package managers;

// mport src.loggerUtils.LoggerManager;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;
import java.io.ByteArrayOutputStream;

public class ClientSendingManager {
	private final int PACKET_SIZE = 1024;
	private final int DATA_SIZE = PACKET_SIZE - 1;
	private final TCPClient tcpClient;

	public ClientSendingManager(TCPClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	public void send(byte[] data)  {
		//var logger = LoggerManager.getLogger(SendingManager.class);
		var t=10;
		for(;;) {
			try {
				while (tcpClient.getSocketChannel().isConnectionPending()){
					tcpClient.getSocketChannel().finishConnect();
				}
				while (!tcpClient.isConnected()){
					System.out.print("not connected, waiting..");
					Thread.sleep(4000);
				}
				
				byte[][] ret = new byte[(int) Math.ceil(data.length / (double) DATA_SIZE)][DATA_SIZE];
				
				int start = 0;
				for (int i = 0; i < ret.length; i++) {
					ret[i] = Arrays.copyOfRange(data, start, start + DATA_SIZE);
					start += DATA_SIZE;
				}
				
				System.out.print("Отправляется " + ret.length + " чанков...");
				
				for (int i = 0; i < ret.length; i++) {
					var chunk = ret[i];
					try (var outputStream = new ByteArrayOutputStream()) {
						outputStream.write(chunk);
						if (i == ret.length - 1) {
							outputStream.write(new byte[]{1});
							tcpClient.getSocketChannel().write(ByteBuffer.wrap(outputStream.toByteArray()));
							System.out.print("Последний чанк размером " + chunk.length + " отправлен на сервер.");
						} else {
							outputStream.write(new byte[]{0});
							tcpClient.getSocketChannel().write(ByteBuffer.wrap(outputStream.toByteArray()));
							System.out.print("Чанк размером " + chunk.length + " отправлен на сервер.");
						}
					}
				}
				return;
			}
			catch (IOException e) {
				if(!e.getMessage().equals("Connection refused: no further information"))
					System.out.println(e);
				if (t--<0) {
					t=10;
					tcpClient.newIP();
				}
				try{
					Thread.sleep(200);
					tcpClient.getSocketChannel().close();
					tcpClient.start();
				} catch (Exception e1){}
			} catch (Exception e1){ break; }
		}
	}
}
