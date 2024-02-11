package managers;

import general.Response;

public class TCPManager {
	private final TCPClient tcpClient;

	public TCPManager(TCPClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	public Response send(String s, Object obj) {
		var r=((Response)tcpClient.send(s,obj));
		if (r==null) {
			try{ Thread.sleep(3000); } catch (Exception e){ System.out.print(e); System.exit(1); }
			r = send(s, obj);
		}
		return r;
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

	public void login(String l, String p) {
		tcpClient.login(l, p);
	}
}
