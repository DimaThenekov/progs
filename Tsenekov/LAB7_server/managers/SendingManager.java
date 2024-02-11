package managers;

// import com.google.common.primitives.Bytes;
// import src.loggerUtils.LoggerManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;
import java.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class SendingManager {
	private static final ExecutorService cachedPool = Executors.newCachedThreadPool();
	private static final Logger LOGGER = LoggerFactory.getLogger("managers.TCPServer");

	private final int PACKET_SIZE = 1024;
	private final int DATA_SIZE = PACKET_SIZE - 1;

	public void send(SocketChannel socketChannel, byte[] data) {
		// var logger = LoggerManager.getLogger(SendingManager.class);
		try {
			byte[][] ret = new byte[(int) Math.ceil(data.length / (double) DATA_SIZE)][DATA_SIZE];

			int start = 0;
			for (int i = 0; i < ret.length; i++) {
				ret[i] = Arrays.copyOfRange(data, start, start + DATA_SIZE);
				start += DATA_SIZE;
			}

			LOGGER.info("Sending " + ret.length + " chunks...");

			for (int i = 0; i < ret.length; i++) {
				var chunk = ret[i];
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				outputStream.write(chunk);
				outputStream.write(new byte[]{(byte)(i == ret.length - 1?1:0)});
				socketChannel.write(ByteBuffer.wrap(outputStream.toByteArray()));
				LOGGER.info(String.valueOf(i+1));
			}
		}
		catch (IOException e){
			if(Objects.equals(e.getMessage(), "Connection reset"))
			{
				LOGGER.info("Connection reset");
				try{
					Thread.sleep(500);
					socketChannel.close();
				} catch (Exception e1){}
			} else
				LOGGER.error(e.getMessage());
		}
	}

	public void send(SocketChannel socketChannel, Object obj) {
		cachedPool.submit(()->{
			try(var baos = new ByteArrayOutputStream(); var a=new ObjectOutputStream(baos)) {
				a.writeObject(obj);
				send(socketChannel, baos.toByteArray());
			} catch (Exception e) {
				System.out.println(e);
			}
		});
	}
}
