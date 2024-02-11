package managers;

import general.Response;

public class TCPManager {
	private final TCPClient tcpClient;

	public TCPManager(TCPClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	public Response send(String s, Object obj) {
		return ((Response)tcpClient.send(s,obj));
	}

	public Response send(String s) {
		return send(s, null);
	}

	public String sendAndGetMassage(String s, Object obj) {
		return send(s, obj).getMassage();
	}

	public String sendAndGetMassage(String s) {
		return send(s).getMassage();
	}
}
