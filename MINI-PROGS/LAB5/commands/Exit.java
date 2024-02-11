package commands;

import utility.Console;
import utility.ExecutionResponse;

/**
 * Команда 'exit'. Завершает выполнение.
 * @author dim0n4eg
 */
public class Exit extends Command {
	private final Console console;

	public Exit(Console console) {
		super("exit", "завершить программу (без сохранения в файл)");
		this.console = console;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public ExecutionResponse apply(String[] arguments) {
		if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		return new ExecutionResponse("exit"); //"Завершение выполнения...");
	}
}
