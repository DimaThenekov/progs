package commands;

import utility.Console;
import managers.CommandManager;


/**
 * Команда 'help'. Выводит справку по доступным командам
 * @author dim0n4eg
 */
public class Help extends Command {
	private final Console console;
	private final CommandManager commandManager;

	public Help(Console console, CommandManager commandManager) {
		super("help", "вывести справку по доступным командам");
		this.console = console;
		this.commandManager = commandManager;
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
		
		commandManager.getCommands().values().forEach(command -> {
			console.printTable(command.getName(), command.getDescription());
		});
		return true;
	}
}
