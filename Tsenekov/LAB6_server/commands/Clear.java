package commands;

import managers.CollectionManager;

import general.Dragon;
import general.Response;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author dim0n4eg
 */
public class Clear extends Command {
	private final CollectionManager collectionManager;

	public Clear(CollectionManager collectionManager) {
		super("clear", "очистить коллекцию");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (!arguments[1].isEmpty())
			return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		var isFirst = true;
		while (collectionManager.getCollection().size() > 0) {
			var dragon = collectionManager.getCollection().getLast();
			collectionManager.remove(dragon.getId());
			collectionManager.addLog("remove " + dragon.getId(),isFirst);
			isFirst = false;
		}
		collectionManager.update();
		return new Response("Коллекция очищена!");
	}
}
