package commands;

import managers.CollectionManager;
import java.util.TreeSet;
import general.Response;
import utility.User;

/**
 * Команда 'print_unique_age'. Вывести уникальные значения поля age всех элементов в коллекции.
 * @author dim0n4eg
 */
public class PrintUniqueAge extends Command {
	private final CollectionManager collectionManager;

	public PrintUniqueAge(CollectionManager collectionManager) {
		super("print_unique_age", "вывести уникальные значения поля age всех элементов в коллекции", "SHOW");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		var beNull = false;
		var ts = new TreeSet<Integer>();
		for (var e : collectionManager.getCollection()) {
			if (e.getAge() == null)
				beNull = true;
			else
				ts.add(e.getAge());
		}
		var s="";
		if (beNull)
			s=" null";
		for (var e : ts)
			s+=" " + e;
		return new Response(s);
	}
}
