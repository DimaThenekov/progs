package commands;

import managers.UserManager;
import managers.CommandManager;

import general.Response;
import general.Dragon;
import utility.User;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Команда 'user_set_functionality'. Удаляет функциональность у роли.
 * @author dim0n4eg
 */
public class NUserRemoveFunctionality extends Command {

	private final UserManager userManager;
	private final CommandManager commandManager;

	public NUserRemoveFunctionality(UserManager userManager, CommandManager commandManager) {
		super("user_remove_functionality role:func", " удаляет функциональность у роли", "PERMISSION");
		this.userManager = userManager;
		this.commandManager = commandManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		try {
			if (arguments[1].trim().equals(":")) return new Response(stats());
			if (userManager.removeFunctionality(arguments[1].split(":")[0], arguments[1].split(":")[1]))
				return new Response("OK\n\n"+stats());
			else
				return new Response(500, "NOOK\n\n"+stats());
		} catch (ArrayIndexOutOfBoundsException e){ return new Response(400, "role:func not valid\n\n"+stats()); }
	}

	private String stats() {
		return "functionality: " + commandManager.getCommands().values().stream().filter(
				command-> !command.getFunctionality().equals("DEFAULT")
			).map(
				command -> command.getFunctionality()
			).distinct().collect(Collectors.joining(","))+",REMOVEALL\n"+
			"role:functionality:\n "+Arrays.stream(userManager.getUsers()).map(
				user -> user.getRole()
			).distinct().map(
				role -> role+":"+userManager.getFunctionality(role)
			).collect(Collectors.joining("\n "));
	}
}
