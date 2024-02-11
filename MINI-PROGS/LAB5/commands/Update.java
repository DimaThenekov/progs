package commands;

import managers.CollectionManager;
import utility.Console;
import models.Ask;
import utility.ExecutionResponse;

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
	public ExecutionResponse apply(String[] arguments) {
		try {
			if (arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
			long id = -1;
			try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { return new ExecutionResponse(false, "ID не распознан"); }
			
			var old = collectionManager.byId(id);
			if (old == null || !collectionManager.getCollection().contains(old)) {
				return new ExecutionResponse(false, "Не существующий ID");
			}
			
			console.println("* Создание нового Дракона:");
			var d = Ask.askDragon(console, old.getId());
			if (d != null && d.validate()) {
				collectionManager.remove(old.getId());
				collectionManager.add(d);
				collectionManager.update();
				return new ExecutionResponse("Обновлено!");
			} else {
				return new ExecutionResponse(false, "Поля Дракона не валидны! Дракон не создан!");
			}
		} catch (Ask.AskBreak e) {
			return new ExecutionResponse(false, "Поля Дракона не валидны! Дракон не создан!");
		}
	}
}
