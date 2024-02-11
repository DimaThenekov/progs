import general.Response;
import managers.TCPClient;
import managers.TCPManager;
import managers.DumpManager;
import managers.CommandManager;
import java.util.Scanner;
import java.util.ArrayList;

import general.Dragon;

import commands.*;


import utility.StandardConsole;
import utility.Runner;
import utility.UserUtility;


public class Main {
	private static int PORT;
	private static String SERVER_ADRESS;
	public static void main(String[] args) {
		var console = new StandardConsole();
		var dumpManager = new DumpManager("client.ini", console); if (!dumpManager.readConf()) { System.exit(1); }
		PORT = dumpManager.getProperty("PORT", -1); if (PORT<0) { console.println("Property PORT undefined"); System.exit(1); }
		
		SERVER_ADRESS = dumpManager.getProperty("SERVER_ADRESS", "127.0.0.1");
		var tcpclient = new TCPClient(SERVER_ADRESS, PORT);
		tcpclient.start();
		var tcpmanager = new TCPManager(tcpclient);
		var userUtility = new UserUtility(tcpmanager, console); if (!userUtility.tryLogin()) { System.exit(1); }
		
		var commandManager = new CommandManager() {{
			register("help", new Help(console, this));
			register("execute_script", new ExecuteScript(console));
			register("exit", new Exit(console));
			//register("show", new Show(console, tcpmanager));
		}};
		
		new Runner(console, commandManager, tcpmanager).interactiveMode();
	}
}

