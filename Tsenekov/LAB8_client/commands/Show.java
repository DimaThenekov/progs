package commands;

import managers.TCPManager;
import utility.Console;
import java.util.stream.Collectors;
import java.util.LinkedList;
import general.Dragon;
import utility.ExitObject;


/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author dim0n4eg
 */
public class Show extends Command {
	private final Console console;
	private final TCPManager tcpManager;

	public Show(Console console, TCPManager tcpManager) {
		super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
		this.console = console;
		this.tcpManager = tcpManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public ExitObject apply(String[] arguments, Object inputObject) {
		if (!arguments[1].isEmpty()) {
			return new ExitObject(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		}
		return new ExitObject(true, tcpManager.send("show").getResponseObj());
	}
}
