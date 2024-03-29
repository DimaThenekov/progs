package commands;

import managers.CollectionManager;

import general.Response;
import utility.User;
import java.util.Arrays;
import java.util.LinkedList;
import general.Dragon;
import java.util.stream.Collectors;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author dim0n4eg
 */
public class Show extends Command {
	private final CollectionManager collectionManager;

	public Show(CollectionManager collectionManager) {
		super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", "SHOW");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		return new Response("OK", new LinkedList<LinkedList<Object>>(new LinkedList<>(Arrays.asList(collectionManager.getCollection().toArray(new Dragon[]{}))).stream().map(x->new LinkedList<Object>(Arrays.asList(x, collectionManager.getUserIdMap().get(x)))).collect(Collectors.toList())));
		//return new Response("OK");
	}
}
