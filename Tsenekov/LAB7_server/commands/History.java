package commands;

import managers.CommandManager;

import general.Response;
import utility.User;
import java.util.stream.Collectors;

/**
 * Команда 'history'. Вывыодит историю команд.
 * @author dim0n4eg
 */
public class History extends Command {
	private final CommandManager commandManager;

	public History(CommandManager commandManager) {
		super("history", "Вывыодит историю команд", "HISTORY");
		this.commandManager = commandManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (!arguments[1].isEmpty()) 
			return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		var s = commandManager.getCommandHistory().stream().map(command -> " " + command).collect(Collectors.joining("\n"));
		
		return new Response(s);
	}
}
