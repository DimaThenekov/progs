package commands;

import managers.TCPManager;
import utility.Console;
import java.util.stream.Collectors;
import java.util.LinkedList;
import general.Dragon;


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
	public boolean apply(String[] arguments) {
		if (!arguments[1].isEmpty()) {
			console.println("Неправильное количество аргументов!");
			console.println("Использование: '" + getName() + "'");
			return false;
		}
		var s=((LinkedList<Dragon>)tcpManager.send("show").getResponseObj()).stream().map(x->x.toString()).collect(Collectors.joining("\n"));
		if (s.isEmpty())
			console.println("Коллекция пуста!");
		else
			console.println(s);
		return true;
	}
}
