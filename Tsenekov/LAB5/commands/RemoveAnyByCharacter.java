package commands;

import managers.CollectionManager;
import utility.Console;
import models.Ask;
import models.DragonCharacter;
import models.Dragon;

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
	public boolean apply(String[] arguments) {
		try {
			if (!arguments[1].isEmpty()) {
				console.println("Неправильное количество аргументов!");
				console.println("Использование: '" + getName() + "'");
				return false;
			}
			
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
				collectionManager.addLog("remove " + d.getId(), true);
				collectionManager.update();
				console.println("Дракон успешно удалён!");
				return true;
			} else {
				console.println("Не найден Character");
				return false;
			}
		} catch (Ask.AskBreak e) {
			console.println("Отмена...");
			return false;
		}
	}
}
