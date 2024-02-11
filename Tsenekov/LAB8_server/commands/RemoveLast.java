package commands;

import managers.CollectionManager;

import general.Dragon;
import general.Response;
import utility.User;

import java.util.NoSuchElementException;

/**
 * Команда 'remove_last'. Удаляет элемент из коллекции.
 * @author dim0n4eg
 */
public class RemoveLast extends Command {

	private final CollectionManager collectionManager;

	public RemoveLast(CollectionManager collectionManager) {
		super("remove_last", "удалить последний элемент из коллекции", "REMOVE");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		try {
			var d = collectionManager.getCollection().getLast();
			if (collectionManager.remove(d.getId(), u))
				return new Response("Дракон успешно удалён!");
			return new Response(500, "Access error");
		} catch (NoSuchElementException e){
			return new Response(400, "Колекция пуста!");
			
		}
	}
}
