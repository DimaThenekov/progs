package commands;

import managers.CollectionManager;
import general.Dragon;

import general.Response;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author dim0n4eg
 */
public class Add extends Command {
	private final CollectionManager collectionManager;

	public Add(CollectionManager collectionManager) {
		super("add {element}", "добавить новый элемент в коллекцию");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		Dragon d = (Dragon)obj;
		
		if (d != null && d.validate()) {
			d.setId(collectionManager.getFreeId());
			collectionManager.add(d);
			collectionManager.addLog("add " + d.getId(), true);
			return new Response("Дракон успешно добавлен!");
		} else {
			return new Response("Поля дракона не валидны! Дракон не создан!");
		}
	}
}
