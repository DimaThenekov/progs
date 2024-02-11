package commands;

import managers.CollectionManager;

import general.Response;

/**
 * Команда 'load'. Загружает коллекцию из файла.
 * @author dim0n4eg
 */
public class Load extends Command {
	private final CollectionManager collectionManager;

	public Load(CollectionManager collectionManager) {
		super("load", "загрузить коллекцию из файла");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		System.out.println("$ load");
		if (!collectionManager.loadCollection()) { System.out.println("Коллекция не загружена!"); }
		return new Response("OK");
	}
}
