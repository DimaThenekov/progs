package commands;

import managers.CollectionManager;
import utility.Console;

/**
 * Команда 'reorder'. Отсортировать коллекцию в порядке, обратном нынешнему.
 * @author dim0n4eg
 */
public class Reorder extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public Reorder(Console console, CollectionManager collectionManager) {
		super("reorder", "отсортировать коллекцию в порядке, обратном нынешнему");
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
		
		collectionManager.addLog("reorder", true);
		collectionManager.isAscendingSort ^= true;
		collectionManager.update();
		return true;
	}
}
