package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.nio.file.*;
import utility.User;

import managers.CommandManager;
import managers.UserManager;
import utility.Console;
import general.Response;
import java.security.MessageDigest;

public class Runner {
	private Console console;
	private final CommandManager commandManager;
	private final UserManager userManager;
	private final List<String> scriptStack = new ArrayList<>();
	private int lengthRecursion = -1;

	public Runner(Console console, CommandManager commandManager, UserManager userManager) {
		this.console = console;
		this.commandManager = commandManager;
		this.userManager = userManager;
	}

	/**
	 * Launchs the command.
	 * @param userCommand Команда для запуска
	 * @return Код завершения.
	 */
	private Response launchCommand(String[] userCommand, Object obj, String login, String pass) {
		if (userCommand[0].equals("")) return new Response("OK");
		var command = commandManager.getCommands().get(userCommand[0]);
		
		if (command == null)
			return new Response(400, "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");//error
		
		var u = userManager.getUser(login);
		if (u != null) {
			MessageDigest md=null;
			try{ md = MessageDigest.getInstance("MD5"); } catch (Exception e) {}
			md.update(pass.getBytes());
			byte[] digest = md.digest();
			String passHash = java.util.HexFormat.of().formatHex(digest);
			if (!passHash.equals(u.getPassword())) return new Response(503, "login or password is invalid");
		}
		if (u == null) u = new User(0, "", "", "");
		System.out.print("123");
		if (!userManager.canEval(u, command.getFunctionality())) return new Response(503, "Permission denied");
		System.out.print("123");
		var resp = command.apply(userCommand, obj, u);
		if (resp == null) return new Response(503, "503");
		return resp;
	}
	
	public Object executeCommand(String s, Object obj, String login, String pass) {
		String[] userCommand = {"", ""};
		userCommand = (s.replace('\n',' ').replace('\r',' ') + " ").split(" ", 2);
		userCommand[1] = userCommand[1].trim();
		System.out.println("$ "+userCommand[0]);
		
		commandManager.addToHistory(userCommand[0]);
		
		return launchCommand(userCommand, obj, login, pass);
	}
}
