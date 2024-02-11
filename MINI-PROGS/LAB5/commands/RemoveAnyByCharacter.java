package commands;

import managers.CollectionManager;
import utility.Console;
import models.Ask;
import models.DragonCharacter;
import models.Dragon;
import utility.ExecutionResponse;

/**
 * Команда 'remove_any_by_character character'. Удалить из коллекции один элемент, значение поля character которого эквивалентно заданному
 * @author dim0n4eg
 */
public class RemoveAnyByCharacter extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public RemoveAnyByCharacter(Console console, CollectionManager collectionManager) {
		super("remove_any_by_character character", "удалить из коллекции один элемент, значение поля character которого эквивалентно заданному");
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
			if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
			var ch = Ask.askDragonCharacter(console, true);
			Dragon d = null;
			for (var e : collectionManager.getCollection()) {
				if ((ch == null && e.getCharacter() == null) || (ch != null && e.getCharacter().equals(ch))) {
					d = e;
					break;
				}
			}
			if (d != null) {
				collectionManager.remove(d.getId());
				collectionManager.update();
				return new ExecutionResponse("Дракон успешно удалён!");
			} else {
				return new ExecutionResponse("Не найден Character");
			}
		} catch (Ask.AskBreak e) {
			return new ExecutionResponse(false, "Отмена...");
		}
	}
}
