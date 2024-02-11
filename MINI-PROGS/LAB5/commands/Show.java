package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author dim0n4eg
 */
public class Show extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public Show(Console console, CollectionManager collectionManager) {
		super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
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
		
		return new ExecutionResponse(collectionManager.toString());
	}
}
