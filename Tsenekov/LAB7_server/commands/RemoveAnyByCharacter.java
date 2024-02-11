package commands;

import managers.CollectionManager;

import general.DragonCharacter;
import general.Dragon;
import general.Response;
import utility.User;

/**
 * Команда 'remove_any_by_character character'. Удалить из коллекции один элемент, значение поля character которого эквивалентно заданному
 * @author dim0n4eg
 */
public class RemoveAnyByCharacter extends Command {

	private final CollectionManager collectionManager;

	public RemoveAnyByCharacter(CollectionManager collectionManager) {
		super("remove_any_by_character {character}", "удалить из коллекции один элемент, значение поля character которого эквивалентно заданному", "REMOVE");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		var ch = (DragonCharacter)obj;
		Dragon d = null;
		for (var e : collectionManager.getCollection()) {
			if ((e.getCharacter() == null && ch == null) || (e.getCharacter() != null && ch != null && e.getCharacter().equals(ch))) {
				d = e;
				break;
			}
		}
		if (d != null) {
			if (collectionManager.remove(d.getId(), u))
				return new Response("Дракон успешно удалён!");
			return new Response(500, "Access error");
		} else {
			return new Response("Не найден Character");
		}
	}
}
