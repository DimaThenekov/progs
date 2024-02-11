package commands;

import utility.Console;
import utility.ExitObject;


/**
 * Команда 'execute_script'. Выполнить скрипт из файла.
 * @author dim0n4eg
 */
public class ExecuteScript extends Command {

	public ExecuteScript() {
		super("execute_script <file_name>", "исполнить скрипт из указанного файла");
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public ExitObject apply(String[] arguments, Object inputObject) {
		if (arguments[1].isEmpty()) {
			return new ExitObject(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		}
		
		return new ExitObject(true, "OK");
	}
}
