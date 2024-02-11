package commands;

import managers.UserManager;

import general.Response;
import general.Dragon;
import utility.User;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Команда 'user_set_role'. Укзазывает пользователю новую роль.
 * @author dim0n4eg
 */
public class NUserSetRole extends Command {

	private final UserManager userManage;

	public NUserSetRole(UserManager userManage) {
		super("user_set_role login:role", "укзатать пользователю новую роль", "PERMISSION");
		this.userManage = userManage;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj, User u) {
		if (arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		try {
			if (arguments[1].trim().equals(":"))
				return new Response(stats());
			if (userManage.getUser(arguments[1].split(":")[0])==null) return new Response("user not found");
			userManage.setRole(userManage.getUser(arguments[1].split(":")[0]), arguments[1].split(":")[1]);
			return new Response("OK");
		} catch (ArrayIndexOutOfBoundsException e){ return new Response(400, "login:role not valid"); }
	}

	private String stats() {
		return "users:\n " + Arrays.stream(userManage.getUsers()).map(
				command -> command.getLogin()+":"+command.getRole()
			).distinct().collect(Collectors.joining("\n "));
	}
}
