package commands;

import managers.CollectionManager;
import utility.Console;
import java.util.TreeSet;
import utility.ExecutionResponse;

/**
 * Команда 'print_unique_age'. Вывести уникальные значения поля age всех элементов в коллекции.
 * @author dim0n4eg
 */
public class PrintUniqueAge extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public PrintUniqueAge(Console console, CollectionManager collectionManager) {
		super("print_unique_age", "вывести уникальные значения поля age всех элементов в коллекции");
		this.console = console;
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public ExecutionResponse apply(String[] arguments) {
		if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		var beNull = false;
		var ts = new TreeSet<Integer>();
		for (var e : collectionManager.getCollection()) {
			if (e.getAge() == null)
				beNull = true;
			else
				ts.add(e.getAge());
		}
		var s="";
		if (beNull)
			s=" null";
		for (var e : ts)
			s+=" " + e;
		return new ExecutionResponse(s);
	}
}
