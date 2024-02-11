package managers;

import java.util.Properties;
import utility.User;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.security.MessageDigest;

/**
 * Использует файл для сохранения и загрузки конфигурации.
 * @author dim0n4eg
 */
public class UserManager {
	private final DumpManager dumpManager;
	private ConcurrentLinkedDeque<User> users = new ConcurrentLinkedDeque<User>();

	public UserManager(DumpManager dumpManager) {
		this.dumpManager = dumpManager;
	}
	
	public boolean init() {
		users.clear();
		for (var e: dumpManager.selectU())
			users.addLast(e);
		return true;
	}

	public User getUser(String login) {
		for (var e: users)
			if (e.getLogin().equals(login))
				return e;
		return null;
	}

	public User[] getUsers() {
		return users.toArray(new User[]{});
	}

	public boolean canEval(User u, String func) {
		if (func.equals("DEFAULT")) return true;
		for (var f: dumpManager.selectF(u.getRole()))
			if (f.equals(func))
				return true;
		return false;
	}

	public boolean addUser(String login, String password) {
		MessageDigest md=null;
		try{ md = MessageDigest.getInstance("MD5"); } catch (Exception e) {}
		md.update(password.getBytes());
		byte[] digest = md.digest();
		String passHash = java.util.HexFormat.of().formatHex(digest);
		var u = new User(0, login, passHash, "defrole");
		if (dumpManager.insertU(u))
			users.addLast(u);
		else
			return false;
		return true;
	}

	public boolean addFunctionality(String role, String funcs) {
		return dumpManager.insertF(role, funcs.split(","));
	}

	public boolean removeFunctionality(String role, String funcs) {
		return dumpManager.removeF(role, funcs.split(","));
	}

	public String getFunctionality(String role) {
		return String.join(",", dumpManager.selectF(role));
	}

	public boolean setRole(User u, String role) {
		u.setRole(role);
		return dumpManager.updateU(u);
	}
}
