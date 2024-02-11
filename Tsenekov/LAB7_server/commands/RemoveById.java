package commands;

import managers.CollectionManager;

import general.Dragon;
import general.Response;
import utility.User;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 * @author dim0n4eg
 */
public class RemoveById extends Command {

	private final CollectionManager collectionManager;

	public RemoveById(CollectionManager collectionManager) {
		super("remove_by_id ID", "удалить элемент из коллекции по ID", "REMOVE");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		long id = -1;
		try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { return new Response(400, "ID не распознан"); }
		
		if (collectionManager.byId(id) == null)
			return new Response(400, "Не существующий ID");
		if (collectionManager.remove(id, u))
			return new Response("Дракон успешно удалён!");
		return new Response(500, "Access error");
	}
}
