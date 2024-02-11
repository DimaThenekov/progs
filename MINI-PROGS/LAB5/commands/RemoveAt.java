package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * Команда 'remove_at'. Удаляет элемент из коллекции.
 * @author dim0n4eg
 */
public class RemoveAt extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public RemoveAt(Console console, CollectionManager collectionManager) {
		super("remove_at index", "удалить элемент, находящийся в заданной позиции коллекции (index)");
		this.console = console;
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public ExecutionResponse apply(String[] arguments) {
		if (arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		int ind = -1;
		try { ind = Integer.parseInt(arguments[1].trim()); } catch (NumberFormatException e) {  return new ExecutionResponse(false, "ID не распознан"); }
		
		try {
			var d = collectionManager.getCollection().get(ind);
			collectionManager.remove(d.getId());
			collectionManager.update();
			return new ExecutionResponse("Дракон успешно удалён!");
		} catch (IndexOutOfBoundsException e) { return new ExecutionResponse(false, "index за границами допустимых значений"); }
	}
}
