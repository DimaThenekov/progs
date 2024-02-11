package commands;

import managers.CollectionManager;
import utility.Console;
import models.Dragon;

/**
 * Команда 'remove_last'. Удаляет элемент из коллекции.
 * @author dim0n4eg
 */
public class RemoveLast extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public RemoveLast(Console console, CollectionManager collectionManager) {
		super("remove_last", "удалить последний элемент из коллекции");
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
		var d = collectionManager.getCollection().getLast();
		collectionManager.remove(d.getId());
		collectionManager.addLog("remove " + d.getId(), true);
		collectionManager.update();
		console.println("Дракон успешно удалён!");
		return true;
	}
}
