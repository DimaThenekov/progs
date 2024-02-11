package commands;

import managers.CollectionManager;
import utility.Console;
import models.Dragon;
import utility.ExecutionResponse;

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
	public ExecutionResponse apply(String[] arguments) {
		if (!arguments[1].isEmpty())
			return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		while (collectionManager.getCollection().size() > 0) {
			var dragon = collectionManager.getCollection().getLast();
			collectionManager.remove(dragon.getId());
		}
		
		collectionManager.update();
		return new ExecutionResponse("Коллекция очищена!");
	}
}
