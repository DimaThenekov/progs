package commands;

import managers.CollectionManager;
import utility.Console;

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
	public boolean apply(String[] arguments) {
		if (!arguments[1].isEmpty()) {
			console.println("Неправильное количество аргументов!");
			console.println("Использование: '" + getName() + "'");
			return false;
		}
		console.println(collectionManager);
		return true;
	}
}
