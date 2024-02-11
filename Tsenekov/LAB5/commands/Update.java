package commands;

import managers.CollectionManager;
import utility.Console;
import models.Ask;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 * @author dim0n4eg
 */
public class Update extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public Update(Console console, CollectionManager collectionManager) {
		super("update <ID> {element}", "обновить значение элемента коллекции по ID");
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
			if (arguments[1].isEmpty()) {
				console.println("Неправильное количество аргументов!");
				console.println("Использование: '" + getName() + "'");
				return false;
			}
			long id = -1;
			try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { console.println("ID не распознан"); return false; }
			
			if (collectionManager.byId(id) == null || !collectionManager.getCollection().contains(collectionManager.byId(id))) {
				console.println("не существующий ID");
				return false;
			}
			
			console.println("* Создание нового Дракона:");
			var d = Ask.askDragon(console, collectionManager);
			if (d != null && d.validate()) {
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
				return true;
			} else {
				console.println("Поля Дракона не валидны! Дракон не создан!");
				return false;
			}
		} catch (Ask.AskBreak e) {
			console.println("Отмена...");
			return false;
		}
	}
}
