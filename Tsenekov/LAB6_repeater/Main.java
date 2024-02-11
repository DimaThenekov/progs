import managers.TCPServer;
import managers.TCPClient;
//import ch.qos.logback.classic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import managers.DumpManager;

import java.util.ArrayList;

// import general.Dragon;

import utility.StandardConsole;

public class Main {
	private static int PORT;
	private static final Logger LOGGER = LoggerFactory.getLogger("Main");
	private static ArrayList<Integer> ServersPorts = new ArrayList<Integer>();
	private static ArrayList<String> ServersAddresses = new ArrayList<String>();
	private static int SelectedSetver = 0;
	
	public static void main(String[] args) {
		initProperty();
		
		var tcpserver = new TCPServer(PORT, (String s, Object o)->{
			LOGGER.info("Geted request");
			Object obj=null;
			while (true) {
				var tcpclient = new TCPClient(ServersAddresses.get(SelectedSetver), ServersPorts.get(SelectedSetver));
				LOGGER.info("Selected server: "+ServersAddresses.get(SelectedSetver)+":"+ServersPorts.get(SelectedSetver).toString());
				tcpclient.start();
				LOGGER.info("Send...");
				obj = tcpclient.send(s, o);
				if (obj != null) { LOGGER.info("Successfully!"); return obj; }
				LOGGER.info("Not successfully... Reconect!");
				SelectedSetver = (SelectedSetver+1) % ServersPorts.size();
				tcpclient.close();
				if (SelectedSetver == 0) { LOGGER.info("Reread property"); initProperty(); }
			}
		});
		
		tcpserver.start();
	}
	
	
	public static void initProperty() {
		var console = new StandardConsole();
		ServersPorts.clear(); ServersAddresses.clear();
		var dumpManager = new DumpManager("repeater.ini", console); if (!dumpManager.readConf()) { System.exit(1); }
		PORT = dumpManager.getProperty("PORT", -1); if (PORT<0) { console.println("Property PORT undefined"); System.exit(1); }
		
		var i = 1;
		while (true) {
			var serverPort = dumpManager.getProperty("SERVER_PORT_"+String.valueOf(i), -1);
			var serverAdress = dumpManager.getProperty("SERVER_ADRESS_"+String.valueOf(i), "127.0.0.1");
			if (serverPort<0 && i==1) {
				console.println("Property SERVER_PORT_1 undefined"); System.exit(1);
			} else if (serverPort<0) {
				break;
			} else {
				ServersPorts.add(serverPort); ServersAddresses.add(serverAdress);
			}
			i++;
		}
	
	}
}


