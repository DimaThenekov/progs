package commands;

import managers.CollectionManager;
import utility.Console;
import models.Dragon;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author dim0n4eg
 */
public class Clear extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public Clear(Console console, CollectionManager collectionManager) {
		super("clear", "очистить коллекцию");
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
		
		var isFirst = true;
		while (collectionManager.getCollection().size() > 0) {
			var dragon = collectionManager.getCollection().removeLast();
			collectionManager.remove(dragon.getId());
			collectionManager.addLog("remove " + dragon.getId(),isFirst);
			isFirst = false;
		}
		
		collectionManager.update();
		console.println("Коллекция очищена!");
		return true;
	}
}
