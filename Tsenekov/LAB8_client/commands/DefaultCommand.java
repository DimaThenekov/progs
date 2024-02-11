package commands;

import utility.Console;
import managers.TCPManager;
import java.util.ArrayList;
import java.util.HashMap;
import utility.Ask;
import general.Dragon;
import general.DragonCharacter;
import utility.ExitObject;


/**
 * Команда
 * @author dim0n4eg
 */
public class DefaultCommand extends Command {
	private static interface ArgExecuter { public String execute(String s, TCPManager tcpm); }
	private static interface AskExecuter { public Object execute(Console cons, Object obj); }

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
	public ExitObject apply(String[] arguments, Object inputObject) {
		var ArgCommandNotUsed = !arguments[1].isEmpty();
		Object sendObj = null;
		if (!args.isEmpty())
			for (var arg: args.split(";"))
				if (argMap.get(arg) != null) {
					var rezArg = argMap.get(arg).execute(arguments[1].trim(), tcpManager);
					if (!rezArg.equals(""))
						return new ExitObject(false, rezArg);
					ArgCommandNotUsed=false;
				} else if (askMap.get(arg) != null) {
					if (ArgCommandNotUsed) break;
					sendObj = askMap.get(arg).execute(console, inputObject);
				} else
					console.printError("Неизвестный тип аргумента: "+arg);
		if (ArgCommandNotUsed) {
			return new ExitObject(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		}
		var res = tcpManager.send(arguments[0]+" "+arguments[1], sendObj);
		return new ExitObject(res.getExitCode()<300, res.getMassage());
	}
	
	private static String argIndex(String s, TCPManager tcpm) {
		int index = -1;
		try { index = Integer.parseInt(s); } catch (NumberFormatException e) { return "index не распознан"; }
		if (index < 0) { return "index < 0"; }
		return "";
	}
	
	private static String argN(String s, TCPManager tcpm) {
		int N = -1;
		try { N = Integer.parseInt(s); } catch (NumberFormatException e) { return "N не распознан"; }
		if (N < 1) { return "N < 1"; }
		return "";
	}
	
	private static String argID(String s, TCPManager tcpm) {
		long id = -1;
		try { id = Long.parseLong(s); } catch (NumberFormatException e) { return "ID не распознан"; }
		if (!tcpm.sendAndGetMassage("is_id_exist "+s).equals("EXIST")) { return "не существет дракона с таким ID"; }
		return "";
	}
	
	private static String argPair(String s, TCPManager tcpm) {
		if (!s.contains(":")) { return "not pair"; }
		return "";
	}
	
	public static Object askDragon(Console cons, Object obj) {
		if (obj == null) {
			try {
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
		} else {
			return (Dragon) obj;
		}
	}
	
	public static Object askCharacter(Console cons, Object obj) {
		if (obj == null) {
			try {
				return Ask.askDragonCharacter(cons, true);
			} catch (Ask.AskBreak e) {
				cons.println("Отмена...");
				return null;
			}
		} else {
			return (DragonCharacter) obj;
		}
	}
}
