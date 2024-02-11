package commands;

import managers.CollectionManager;
import models.Dragon;
import utility.Console;
import utility.ExecutionResponse;

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
	public ExecutionResponse apply(String[] arguments) {
		if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		Dragon mymax = null;
		for (var e : collectionManager.getCollection()) {
			if (mymax==null || mymax.getCharacter().ordinal() > mymax.getCharacter().ordinal()) mymax = e;
		}
		if (mymax == null) {
			return new ExecutionResponse("Драконов не обнаружено.");
		} else {
			return new ExecutionResponse(mymax.toString());
		}
	}
}
