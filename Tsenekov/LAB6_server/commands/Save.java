package commands;

import managers.CollectionManager;

import general.Response;

/**
 * Команда 'save'. Сохраняет коллекцию в файл.
 * @author dim0n4eg
 */
public class Save extends Command {
	private final CollectionManager collectionManager;

	public Save(CollectionManager collectionManager) {
		super("save", "сохранить коллекцию в файл");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		System.out.println("$ save");
		collectionManager.saveCollection();
		return new Response("OK");
	}
}
