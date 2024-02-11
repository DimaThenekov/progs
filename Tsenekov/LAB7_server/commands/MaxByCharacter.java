package commands;

import managers.CollectionManager;
import general.Dragon;
import utility.User;
import general.Response;

/**
 * Команда 'max_by_character'. Вывести любой объект из коллекции, значение поля character которого является максимальным.
 * @author dim0n4eg
 */
public class MaxByCharacter extends Command {
	private final CollectionManager collectionManager;

	public MaxByCharacter(CollectionManager collectionManager) {
		super("max_by_character", "ввывести любой объект из коллекции, значение поля character которого является максимальным", "SHOW");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		Dragon mymax = null;
		for (var e : collectionManager.getCollection()) {
			if (mymax==null || mymax.getCharacter().ordinal() > mymax.getCharacter().ordinal()) mymax = e;
		}
		if (mymax == null) {
			return new Response("Драконов не обнаружено.");
		} else {
			return new Response(mymax.toString());
		}
	}
}
