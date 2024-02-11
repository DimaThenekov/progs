package commands;

import managers.CollectionManager;

import general.Response;
import utility.User;


/**
 * Команда 'is_id_exist'.
 * @author dim0n4eg
 */
public class IsIdExist extends Command {
	private final CollectionManager collectionManager;

	public IsIdExist(CollectionManager collectionManager) {
		super("is_id_exist", "сообщает о существовании id", "SHOW");
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
		
		if (collectionManager.byId(id) == null || !collectionManager.getCollection().contains(collectionManager.byId(id)))
			return new Response("NOT_EXIST");
		else
			return new Response("EXIST");
	}
}
