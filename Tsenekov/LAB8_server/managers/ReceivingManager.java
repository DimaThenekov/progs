package managers;

// import src.loggerUtils.LoggerManager;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.io.ByteArrayOutputStream;
// import com.google.common.primitives.Bytes;
// import org.apache.commons.lang3.tuple.ImmutablePair;
// import org.apache.commons.lang3.tuple.Pair;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.Consumer;
import java.util.function.Consumer;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ReceivingManager {
	private static final ExecutorService cachedPool = Executors.newCachedThreadPool();
	private static final Logger LOGGER = LoggerFactory.getLogger("managers.TCPServer");
	private HashSet<SocketChannel> sessions = new HashSet<>();
	private final HashMap<SocketAddress, byte[]> receivedData;
	
	public ReceivingManager(){
		this.receivedData = new HashMap<>();
	}
	
	public byte[] read(SelectionKey key) {
		SocketChannel channel = (SocketChannel) key.channel();
		try {
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			int numRead = channel.read(byteBuffer);
			if (numRead == -1) {
				this.sessions.remove(channel);
				LOGGER.info("client " + channel.socket().getRemoteSocketAddress() + " disconnected");
				channel.close();
				key.cancel();
				return null;
			}
			var clientSocket = channel.socket();
			var arr = receivedData.get(clientSocket.getRemoteSocketAddress());
			arr = arr == null ? new byte[0] : arr;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write(arr);
			outputStream.write(Arrays.copyOf(byteBuffer.array(), byteBuffer.array().length - 1));
			arr = outputStream.toByteArray();
			if(byteBuffer.array()[numRead-1] == 1){
				receivedData.put(clientSocket.getRemoteSocketAddress(), new byte[0]);
				return arr;
			}
			receivedData.put(clientSocket.getRemoteSocketAddress(), arr);
		}
		catch (IOException e){
			System.out.println(e.getMessage());
			if(Objects.equals(e.getMessage(), "Connection reset"))
			{
				try{
					Thread.sleep(500);
					channel.close();
				} catch (Exception e1){}
			}
		}
		return null;
	}
	
	public void read(SelectionKey key, Consumer<ObjectInputStream> callback) {
		cachedPool.submit(()->{
			var result = read(key);
			if(result == null) return;
			//Request request;
			// object is done being transferred
			var res = result;
			int p = -1;
			
			for (int i = res.length - 1; i > -1; i--) {
				if (res[i] != 0) {
					p = i;
					break;
				}
			}
			var cutres = Arrays.copyOfRange(res, 0, p+1);
			// System.out.println(p);
			try (var ois = new ObjectInputStream(new ByteArrayInputStream(cutres))) {
				callback.accept(ois);
			} catch (IOException e) {}
		});
	}
}
