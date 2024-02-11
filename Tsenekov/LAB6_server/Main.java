import managers.TCPServer;
//import ch.qos.logback.classic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import commands.*;

import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;

// import general.Dragon;

import utility.StandardConsole;
import utility.ClientConsole;
import utility.Runner;

public class Main {
	private static int PORT;
	private static final Logger LOGGER = LoggerFactory.getLogger("Main");
	
	public static void main(String[] args) {
		var console = new StandardConsole();
		var dumpManager = new DumpManager("test.csv", console);
		var collectionManager = new CollectionManager(dumpManager);
		if (!collectionManager.loadCollection()) { System.out.println("Коллекция не загружена!"); }
		PORT = dumpManager.getProperty("PORT", -1);
		if (PORT<0) { System.out.println("Property PORT undefined"); System.exit(1); }
		
		var commandManager = new CommandManager() {{
			register("get_commands", new GetCommands(this));
			register("history", new History(this));
			register("load", new Load(collectionManager));
			register("info", new Info(collectionManager));
			register("show", new Show(collectionManager));
			register("add", new Add(collectionManager));
			register("clear", new Clear(collectionManager));
			register("save", new Save(collectionManager));
			register("max_by_character", new MaxByCharacter(collectionManager));
			register("print_unique_age", new PrintUniqueAge(collectionManager));
			register("remove_any_by_character", new RemoveAnyByCharacter(collectionManager));
			register("remove_at", new RemoveAt(collectionManager));
			register("remove_by_id", new RemoveById(collectionManager));
			register("remove_last", new RemoveLast(collectionManager));
			register("reorder", new Reorder(collectionManager));
			register("undo", new Undo(collectionManager));
			register("update", new Update(collectionManager));
			register("is_id_exist", new IsIdExist(collectionManager));
		}};
		
		var runner = new Runner(console, commandManager);
		
		var tcpserver = new TCPServer(PORT, runner::executeCommand);
		tcpserver.start();
	}
}

