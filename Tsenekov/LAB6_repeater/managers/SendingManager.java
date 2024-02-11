package managers;

// import com.google.common.primitives.Bytes;
// import src.loggerUtils.LoggerManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;
import java.io.ByteArrayOutputStream;

public class SendingManager {

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

			System.out.print("Sending " + ret.length + " chunks...");

			for (int i = 0; i < ret.length; i++) {
				var chunk = ret[i];
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				outputStream.write(chunk);
				outputStream.write(new byte[]{(byte)(i == ret.length - 1?1:0)});
				socketChannel.write(ByteBuffer.wrap(outputStream.toByteArray()));
				System.out.print(" ");
				System.out.print(i+1);
			}
			System.out.println("");
		}
		catch (IOException e){
			if(Objects.equals(e.getMessage(), "Connection reset"))
			{
				System.out.println("Connection reset");
				try{
					Thread.sleep(500);
					socketChannel.close();
				} catch (Exception e1){}
			} else
				System.out.println(e.getMessage());
		}
	}
}
