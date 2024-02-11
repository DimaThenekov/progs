package commands;

import managers.CollectionManager;
import utility.Console;
import models.Dragon;
import utility.ExecutionResponse;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 * @author dim0n4eg
 */
public class RemoveById extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public RemoveById(Console console, CollectionManager collectionManager) {
		super("remove_by_id <ID>", "удалить элемент из коллекции по ID");
		this.console = console;
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public ExecutionResponse apply(String[] arguments) {
		if (arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		long id = -1;
		try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { return new ExecutionResponse(false, "ID не распознан"); }
		
		if (collectionManager.byId(id) == null || !collectionManager.getCollection().contains(collectionManager.byId(id)))
			return new ExecutionResponse(false, "Не существующий ID");
		collectionManager.remove(id);
		collectionManager.update();
		return new ExecutionResponse("Дракон успешно удалён!");
	}
}
