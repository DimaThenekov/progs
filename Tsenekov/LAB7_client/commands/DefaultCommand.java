package commands;

import utility.Console;
import managers.TCPManager;
import java.util.ArrayList;
import java.util.HashMap;
import utility.Ask;
import general.Dragon;


/**
 * Команда
 * @author dim0n4eg
 */
public class DefaultCommand extends Command {
	private static interface ArgExecuter { public boolean execute(String s, Console cons, TCPManager tcpm); }
	private static interface AskExecuter { public Object execute(Console cons); }

	private final Console console;
	private final TCPManager tcpManager;
	private final String args;
	private static HashMap<String, ArgExecuter> argMap = new HashMap<>();
	private static HashMap<String, AskExecuter> askMap = new HashMap<>();

	static {
		argMap.put("index", DefaultCommand::argIndex);
		argMap.put("N", DefaultCommand::argN);
		argMap.put("ID", DefaultCommand::argID);
		argMap.put("role:func", DefaultCommand::argPair);
		argMap.put("login:role", DefaultCommand::argPair);
		
		askMap.put("{element}", DefaultCommand::askDragon);
		askMap.put("{character}", DefaultCommand::askCharacter);
	}

	public DefaultCommand(String[] command, Console console, TCPManager tcpManager) {
		super(command[1], command[3]);
		args = command[2];
		this.console = console;
		this.tcpManager = tcpManager;
		
		
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public boolean apply(String[] arguments) {
		var ArgCommandNotUsed = !arguments[1].isEmpty();
		Object sendObj = null;
		if (!args.isEmpty())
			for (var arg: args.split(";"))
				if (argMap.get(arg) != null) {
					if (!argMap.get(arg).execute(arguments[1].trim(), console, tcpManager))
						return false;
					ArgCommandNotUsed=false;
				} else if (askMap.get(arg) != null) {
					if (ArgCommandNotUsed) break;
					sendObj = askMap.get(arg).execute(console);
				} else
					console.printError("Неизвестный тип аргумента: "+arg);
		if (ArgCommandNotUsed) {
			console.println("Неправильное количество аргументов!");
			console.println("Использование: '" + getName() + "'");
			return false;
		}
		var res = tcpManager.send(arguments[0]+" "+arguments[1], sendObj);
		console.println(res.getMassage());
		return res.getExitCode()<300;
	}
	
	private static boolean argIndex(String s, Console cons, TCPManager tcpm) {
		int index = -1;
		try { index = Integer.parseInt(s); } catch (NumberFormatException e) { cons.println("index не распознан"); return false; }
		if (index < 0) { cons.println("index < 0"); return false; }
		return true;
	}
	
	private static boolean argN(String s, Console cons, TCPManager tcpm) {
		int N = -1;
		try { N = Integer.parseInt(s); } catch (NumberFormatException e) { cons.println("N не распознан"); return false; }
		if (N < 1) { cons.println("N < 1"); return false; }
		return true;
	}
	
	private static boolean argID(String s, Console cons, TCPManager tcpm) {
		long id = -1;
		try { id = Long.parseLong(s); } catch (NumberFormatException e) { cons.println("ID не распознан"); return false; }
		if (!tcpm.sendAndGetMassage("is_id_exist "+s).equals("EXIST")) { cons.println("не существет дракона с таким ID"); return false; }
		return true;
	}
	
	private static boolean argPair(String s, Console cons, TCPManager tcpm) {
		if (!s.contains(":")) { cons.println("not pair"); return false; }
		return true;
	}
	
	public static Object askDragon(Console cons) {
		try {
			cons.println("* Создание Дракона:");
			Dragon d = Ask.askDragon(cons);
			
			if (d != null && d.validate()) {
				return d;
			} else {
				cons.println("Поля дракона не валидны! Дракон не создан!");
				return null;
			}
		} catch (Ask.AskBreak e) {
			cons.println("Отмена...");
			return null;
		}
	}
	
	public static Object askCharacter(Console cons) {
		try {
			return Ask.askDragonCharacter(cons, true);
		} catch (Ask.AskBreak e) {
			cons.println("Отмена...");
			return null;
		}
	}
}
