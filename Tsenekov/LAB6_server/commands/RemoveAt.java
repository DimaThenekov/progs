package commands;

import managers.CollectionManager;

import general.Response;

/**
 * Команда 'remove_at'. Удаляет элемент из коллекции.
 * @author dim0n4eg
 */
public class RemoveAt extends Command {

	private final CollectionManager collectionManager;

	public RemoveAt(CollectionManager collectionManager) {
		super("remove_at index", "удалить элемент, находящийся в заданной позиции коллекции (index)");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		int ind = -1;
		try { ind = Integer.parseInt(arguments[1].trim()); } catch (NumberFormatException e) { return new Response(400, "ID не распознан"); }
		
		try {
			var d = collectionManager.getCollection().get(ind);
			collectionManager.remove(d.getId());
			collectionManager.addLog("remove " + d.getId(), true);
			collectionManager.update();
			return new Response("Дракон успешно удалён!");
		} catch (IndexOutOfBoundsException e) { return new Response(400, "index за границами допустимых значений"); }
	}
}
