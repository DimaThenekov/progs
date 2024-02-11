package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.nio.file.*;

import managers.CommandManager;
import managers.TCPManager;
import commands.Show;
import commands.DefaultCommand;
import utility.ExitObject;
import java.util.Map;
import commands.Command;

public class Runner {
	
	private Console console = new StandardConsole();
	private final CommandManager commandManager;
	private final TCPManager tcpManager;
	private final List<String> scriptStack = new ArrayList<>();
	private int lengthRecursion = 10;

	public Runner(CommandManager commandManager, TCPManager tcpManager) {
		this.commandManager = commandManager;
		this.tcpManager = tcpManager;
	}

	public ExitObject launchCommand(String command, String argument, Object obj) {
		String[] userCommand = {command.trim(), argument.trim()};
		
		commandManager.addToHistory(userCommand[0]);
		return launchCommand(userCommand, obj);
	}

	public ExitObject launchCommand(String command, String argument) {
		return launchCommand(command, argument, null);
	}

	public ExitObject launchCommand(String command) {
		return launchCommand(command,"");
	}

	public boolean login(String login, String password) {
		tcpManager.login(login, password);
		if (tcpManager.sendAndGetMassage("get_commands").equals("OK")) return true;
		return false;
	}

	public boolean register(String login, String password) {
		tcpManager.sendAndGetMassage("create_user "+login+":"+password);
		return login(login, password);
	}

	public String getLogin() {
		return tcpManager.getLogin();
	}

	public Map<String, Command> getCommands() { return commandManager.getCommands(); }

	/**
	 * Режим для запуска скрипта.
	 * @param argument Аргумент скрипта
	 * @return Код завершения.
	 */
	public ExitObject scriptMode(String argument) {
		String[] userCommand = {"", ""};
		scriptStack.add(argument);
		if (!new File(argument).exists()) return new ExitObject(false, "Файл не существет!");
		if (!Files.isReadable(Paths.get(argument))) return new ExitObject(false, "Прав для чтения нет!");
		
		try (Scanner scriptScanner = new Scanner(new File(argument))) {
			if (!scriptScanner.hasNext()) throw new NoSuchElementException();
			console.selectFileScanner(scriptScanner);
			var needwork = false;
			do {
				userCommand = (console.readln().trim() + " ").split(" ", 2);
				userCommand[1] = userCommand[1].trim();
				while (console.isCanReadln() && userCommand[0].isEmpty()) {
					userCommand = (console.readln().trim() + " ").split(" ", 2);
					userCommand[1] = userCommand[1].trim();
				}
				var needLaunch = true;
				if (userCommand[0].equals("execute_script")) {
					var recStart = -1;
					var i = 0;
					for (String script : scriptStack) {
						i++;
						if (userCommand[1].equals(script)) {
							if (recStart < 0) recStart = i;
							if (i > recStart + lengthRecursion || i > 500)
								needLaunch = false;
						}
					}
				}
				needwork = needLaunch ? launchCommand(userCommand, null).isSuccessfully : true;
			} while (needwork && console.isCanReadln());
			
			console.selectConsoleScanner();
			if (!needwork && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
				return new ExitObject(false,"Проверьте скрипт на корректность введенных данных!");
			}
			
			return new ExitObject(needwork, needwork?"OK":"NOT OK");
		} catch (FileNotFoundException exception) {
			return new ExitObject(false,"Файл со скриптом не найден!");
			//console.printError(exception.toString());
		} catch (NoSuchElementException exception) {
			return new ExitObject(false,"Файл со скриптом пуст!");
		} catch (IllegalStateException exception) {
			return new ExitObject(false,"Непредвиденная ошибка!");
		} finally {
			scriptStack.remove(scriptStack.size() - 1);
		}
		// return new ExitObject(false,"OK");
	}

	/**
	 * Launchs the command.
	 * @param userCommand Команда для запуска
	 * @return Код завершения.
	 */
	private ExitObject launchCommand(String[] userCommand, Object inputObject) {
		commandManager.clear();
		for (var e:(ArrayList<String[]>)tcpManager.send("get_commands").getResponseObj())
			if (e[0].equals("show"))
				commandManager.register("$show", new Show(console, tcpManager));
			else
				commandManager.register("$"+e[0], new DefaultCommand(e, console, tcpManager));
		
		if (userCommand[0].equals("")) return new ExitObject(true, null);
		var command = commandManager.getCommands().get(userCommand[0]);
		if (command == null) command = commandManager.getCommands().get('$'+userCommand[0]);
		if (command == null) return new ExitObject(false, "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
		
		switch (userCommand[0]) {
			case "execute_script" -> { return scriptMode(userCommand[1]); }
			default -> { return command.apply(userCommand, inputObject); }
		}
	}
}
