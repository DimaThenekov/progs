package commands;

import managers.CollectionManager;

import general.Dragon;
import general.Response;
import utility.User;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author dim0n4eg
 */
public class Clear extends Command {
	private final CollectionManager collectionManager;

	public Clear(CollectionManager collectionManager) {
		super("clear", "очистить коллекцию", "REMOVE");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		for (var e: collectionManager.getCollection().toArray()) { collectionManager.remove(((Dragon)e).getId(), u); }
		collectionManager.update();
		return new Response("Коллекция очищена!");
	}
}
