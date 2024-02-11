package commands;

import managers.CommandManager;
import utility.Console;
import utility.ExecutionResponse;
import java.util.stream.Collectors;

/**
 * Команда 'history'. Вывыодит историю команд.
 * @author dim0n4eg
 */
public class History extends Command {
	private final Console console;
	private final CommandManager commandManager;

	public History(Console console, CommandManager commandManager) {
		super("history", "Вывыодит историю команд");
		this.console = console;
		this.commandManager = commandManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public ExecutionResponse apply(String[] arguments) {
		if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		return new ExecutionResponse(commandManager.getCommandHistory().stream().map(command -> " " + command).collect(Collectors.joining("\n")));
	}
}
