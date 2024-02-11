package commands;

import managers.CollectionManager;

import general.Dragon;
import general.Response;

import java.util.NoSuchElementException;

/**
 * Команда 'remove_last'. Удаляет элемент из коллекции.
 * @author dim0n4eg
 */
public class RemoveLast extends Command {

	private final CollectionManager collectionManager;

	public RemoveLast(CollectionManager collectionManager) {
		super("remove_last", "удалить последний элемент из коллекции");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		try {
		var d = collectionManager.getCollection().getLast();
		collectionManager.remove(d.getId());
		collectionManager.addLog("remove " + d.getId(), true);
		collectionManager.update();
		return new Response("Дракон успешно удалён!");
		} catch (NoSuchElementException e){
			return new Response(400, "Колекция пуста!");
			
		}
	}
}
