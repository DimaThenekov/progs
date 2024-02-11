package commands;

import managers.CollectionManager;
import utility.Console;
import models.Dragon;
import utility.ExecutionResponse;

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
	public ExecutionResponse apply(String[] arguments) {
		if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		var d = collectionManager.getCollection().getLast();
		collectionManager.remove(d.getId());
		collectionManager.update();
		return new ExecutionResponse("Дракон успешно удалён!");
	}
}
