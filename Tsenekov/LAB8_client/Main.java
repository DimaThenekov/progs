import com.formdev.flatlaf.FlatDarkLaf;

import GUI.GUILogin;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import managers.LangManager;
import utility.StandardConsole;
import managers.DumpManager;
import managers.CommandManager;
import commands.*;
import managers.TCPClient;
import managers.TCPManager;
import utility.Runner;

/**
 * Combo box demo. Created by sasha on 12/02/16.
 */
public class Main {
	public static void main(String[] args) {
		try { UIManager.setLookAndFeel(new FlatDarkLaf()); } catch (UnsupportedLookAndFeelException e) { e.printStackTrace(); } // Делаем красиво 
		var lm = new LangManager();  // Делаем переводчик
		
		var console = new StandardConsole(); // Для вывода ошибок
		var dumpManager = new DumpManager("client.ini", console); if (!dumpManager.readConf()) { System.exit(1); }
		var PORT = dumpManager.getProperty("PORT", -1); if (PORT<0) { console.println("Property PORT undefined"); System.exit(1); }
		
		var tcpclient = new TCPClient(dumpManager.getProperty("SERVER_ADRESS", "127.0.0.1"), PORT); tcpclient.start();
		var tcpmanager = new TCPManager(tcpclient);
		
		var commandManager = new CommandManager() {{
			register("execute_script", new ExecuteScript());
		}};
		
		new GUILogin(lm, new Runner(commandManager, tcpmanager)); // Запускаем форму
	}
}
