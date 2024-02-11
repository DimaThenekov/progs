package commands;

import utility.Console;

/**
 * Команда 'execute_script'. Выполнить скрипт из файла.
 * @author dim0n4eg
 */
public class ExecuteScript extends Command {
	private final Console console;

	public ExecuteScript(Console console) {
		super("execute_script <file_name>", "исполнить скрипт из указанного файла");
		this.console = console;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public boolean apply(String[] arguments) {
		if (arguments[1].isEmpty()) {
			console.println("Неправильное количество аргументов!");
			console.println("Использование: '" + getName() + "'");
			return false;
		}
		
		console.println("Выполнение скрипта '" + arguments[1] + "'...");
		return true;
	}
}
