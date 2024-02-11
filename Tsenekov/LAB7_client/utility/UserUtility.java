package utility;

import java.util.Scanner;
import managers.TCPManager;
import utility.Console;

public class UserUtility {
	private final TCPManager tcpManager;
	private final Console console;

	public UserUtility(TCPManager tcpManager, Console console) {
		this.tcpManager = tcpManager;
		this.console = console;
	}

	public boolean tryLogin() {
		for (;;) {
			console.print("Do you want to register account? (Press Y): "); 
			var isReg = (console.readln().trim().equals("Y"));
			console.print("Login: "); 
			var login = console.readln().trim();
			console.print("Password: "); 
			var password = console.readln().trim();
			tcpManager.login(login, password);
			if (isReg) { 
				console.println("creating...");
				console.println(tcpManager.sendAndGetMassage("create_user "+login+":"+password));
			}
			if (tcpManager.sendAndGetMassage("get_commands").equals("OK")) return true;
			console.println("not logined");
		}
	}
}
