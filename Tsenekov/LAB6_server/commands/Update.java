package commands;

import managers.CollectionManager;

import general.Response;
import general.Dragon;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 * @author dim0n4eg
 */
public class Update extends Command {

	private final CollectionManager collectionManager;

	public Update(CollectionManager collectionManager) {
		super("update ID {element}", "обновить значение элемента коллекции по ID");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		long id = -1;
		try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { return new Response(400, "ID не распознан"); }
		
		if (collectionManager.byId(id) == null || !collectionManager.getCollection().contains(collectionManager.byId(id))) {
			return new Response(400, "Не существующий ID");
		}
		
		var d = (Dragon)obj;
		if (d != null && d.validate()) {
			d.setId(collectionManager.getFreeId());
			collectionManager.add(d);
			collectionManager.addLog("add " + d.getId(), true);
			collectionManager.update();
			
			var old = collectionManager.byId(id);
			collectionManager.swap(d.getId(), id);
			collectionManager.addLog("swap " + old.getId() + " " + id, false);
			collectionManager.update();
			
			collectionManager.remove(old.getId());
			collectionManager.addLog("remove " + old.getId(), false);
			collectionManager.update();
			return new Response("Обновлено!");
		} else {
			return new Response(400, "Поля Дракона не валидны! Дракон не создан!");
		}
	}
}
