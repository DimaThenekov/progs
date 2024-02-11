package commands;

import managers.CollectionManager;

import general.Response;
import utility.User;
import general.Dragon;

/**
 * Команда 'remove_at'. Удаляет элемент из коллекции.
 * @author dim0n4eg
 */
public class RemoveAt extends Command {

	private final CollectionManager collectionManager;

	public RemoveAt(CollectionManager collectionManager) {
		super("remove_at index", "удалить элемент, находящийся в заданной позиции коллекции (index)", "REMOVE");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		int ind = -1;
		try { ind = Integer.parseInt(arguments[1].trim()); } catch (NumberFormatException e) { return new Response(400, "ID не распознан"); }
		
		try {
			var d = (Dragon)collectionManager.getCollection().toArray()[ind];
			if (collectionManager.remove(d.getId(), u))
				return new Response("Дракон успешно удалён!");
			return new Response(500, "Access error");
		} catch (IndexOutOfBoundsException e) { return new Response(400, "index за границами допустимых значений"); }
	}
}
