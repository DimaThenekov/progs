package commands;

import managers.CollectionManager;
import utility.Console;

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
	public boolean apply(String[] arguments) {
		if (arguments[1].isEmpty()) {
			console.println("Неправильное количество аргументов!");
			console.println("Использование: '" + getName() + "'");
			return false;
		}
		int ind = -1;
		try { ind = Integer.parseInt(arguments[1].trim()); } catch (NumberFormatException e) { console.println("ID не распознан"); return false; }
		
		try {
			var d = collectionManager.getCollection().get(ind);
			collectionManager.remove(d.getId());
			collectionManager.addLog("remove " + d.getId(), true);
			collectionManager.update();
			console.println("Дракон успешно удалён!");
			return true;
		} catch (IndexOutOfBoundsException e) { console.println("index за границами допустимых значений"); return false; }
	}
}
