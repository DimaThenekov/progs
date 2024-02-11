package commands;

import managers.CollectionManager;
import models.Ask;
import models.Dragon;
import utility.Console;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author dim0n4eg
 */
public class Add extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public Add(Console console, CollectionManager collectionManager) {
		super("add {element}", "добавить новый элемент в коллекцию");
		this.console = console;
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public boolean apply(String[] arguments) {
		try {
			if (!arguments[1].isEmpty()) {
				console.println("Неправильное количество аргументов!");
				console.println("Использование: '" + getName() + "'");
				return false;
			}
			
			console.println("* Создание нового Дракона:");
			Dragon d = Ask.askDragon(console, collectionManager);
			
			if (d != null && d.validate()) {
				collectionManager.add(d);
				collectionManager.addLog("add " + d.getId(), true);
				console.println("Дракон успешно добавлен!");
				return true;
			} else {
				console.printError("Поля дракона не валидны! Дракон не создан!");
				return false;
			}
		} catch (Ask.AskBreak e) {
			console.println("Отмена...");
			return false;
		}
	}
}
