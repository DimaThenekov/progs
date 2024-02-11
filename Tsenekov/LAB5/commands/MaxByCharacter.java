package commands;

import managers.CollectionManager;
import models.Dragon;
import utility.Console;

/**
 * Команда 'max_by_character'. Вывести любой объект из коллекции, значение поля character которого является максимальным.
 * @author dim0n4eg
 */
public class MaxByCharacter extends Command {
	private final Console console;
	private final CollectionManager collectionManager;

	public MaxByCharacter(Console console, CollectionManager collectionManager) {
		super("max_by_character", "ввывести любой объект из коллекции, значение поля character которого является максимальным");
		this.console = console;
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public boolean apply(String[] arguments) {
		if (!arguments[1].isEmpty()) {
			console.println("Неправильное количество аргументов!");
			console.println("Использование: '" + getName() + "'");
			return false;
		}
		Dragon mymax = null;
		for (var e : collectionManager.getCollection()) {
			if (mymax==null || mymax.getCharacter().ordinal() > mymax.getCharacter().ordinal()) mymax = e;
		}
		if (mymax == null) {
			console.println("Драконов не обнаружено.");
		} else {
			console.println(mymax.toString() + "\n");
		}
		return true;
	}
}
