package commands;

import managers.CollectionManager;
import utility.Console;

/**
 * Команда 'undo'. Отменяет действие команд.
 * @author dim0n4eg
 */
public class Undo extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public Undo(Console console, CollectionManager collectionManager) {
		super("undo <n>", "Отменяет действие n последних команд");
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
		int n = -1;
		try { n = Integer.parseInt(arguments[1].trim()); } catch (NumberFormatException e) { console.println("n не распознан"); return false; }
		
		if (n < 1) {
			console.println("n < 1");
			return false;
		}
		
		var i = collectionManager.undo(n);
		console.println("Удалось отменить " + i + " команд");
		return i==n;
	}
}
