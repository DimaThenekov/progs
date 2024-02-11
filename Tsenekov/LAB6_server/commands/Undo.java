package commands;

import managers.CollectionManager;

import general.Response;

/**
 * Команда 'undo'. Отменяет действие команд.
 * @author dim0n4eg
 */
public class Undo extends Command {

	private final CollectionManager collectionManager;

	public Undo(CollectionManager collectionManager) {
		super("undo N", "Отменяет действие N последних команд");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		int n = -1;
		try { n = Integer.parseInt(arguments[1].trim()); } catch (NumberFormatException e) { return new Response(400, "N не распознан"); }
		
		if (n < 1) return new Response(400, "N < 1");
		
		var i = collectionManager.undo(n);
		return new Response(i==n?200:503, "Удалось отменить " + i + " команд");
	}
}
