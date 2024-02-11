package commands;

import managers.CommandManager;

import general.Response;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Команда 'get_commands'. Выводит все элементы коллекции.
 * @author dim0n4eg
 */
public class GetCommands extends Command {
	private final CommandManager commandManager;

	public GetCommands(CommandManager commandManager) {
		super("get_commands", "вывести список серверных команд");
		this.commandManager = commandManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		var s = new ArrayList<>(Arrays.asList(
			commandManager.getCommands().values().stream().filter(
				command-> !command.getName().equals("get_commands") &&
					!command.getName().equals("is_id_exist") &&
					!command.getName().equals("save") &&
					!command.getName().equals("load") &&
					!command.getName().equals("show")
			).map(
				command -> new String[]{
					(command.getName()+" ").split(" ",2)[0], // command
					command.getName(), // command for help
					(command.getName()+" ").split(" ",2)[1].trim().replace(' ', ','), // arguments
					command.getDescription() // description
				}
			).toArray()));
		
		return new Response("OK",s);
	}
}
