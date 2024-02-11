package managers;

import java.util.LinkedList;
import java.util.Stack;
import models.Dragon;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Оперирует коллекцией.
 * @author dim0n4eg
 */
public class CollectionManager {
	private long currentId = 1;
	private Map<Long, Dragon> dragons = new HashMap<>();
	private ArrayDeque<String> logStack = new ArrayDeque<String>();

	private LinkedList<Dragon> collection = new LinkedList<Dragon>();
	private LinkedList<Dragon> collectionDie = new LinkedList<Dragon>();
	private LocalDateTime lastInitTime;
	private LocalDateTime lastSaveTime;
	private final DumpManager dumpManager;
	public boolean isAscendingSort;

	public CollectionManager(DumpManager dumpManager) {
		this.lastInitTime = null;
		this.lastSaveTime = null;
		this.dumpManager = dumpManager;
	}

	/**
	 * @return коллекция.
	 */
	public LinkedList<Dragon> getCollection() {
		return collection;
	}

	/**
	 * @return Последнее время инициализации.
	 */
	public LocalDateTime getLastInitTime() {
		return lastInitTime;
	}

	/**
	 * @return Последнее время сохранения.
	 */
	public LocalDateTime getLastSaveTime() {
		return lastSaveTime;
	}

	/**
	 * Сохраняет коллекцию в файл
	 */
	public void saveCollection() {
		dumpManager.writeCollection(collection, collectionDie, logStack);
		lastSaveTime = LocalDateTime.now();
	}

	/**
	 * Получить дракона по ID
	 */
	public Dragon byId(Long id) { return dragons.get(id); }

	/**
	 * Содержит ли колекции дракона
	 */
	public boolean isСontain(Dragon e) { return e == null || byId(e.getId()) != null; }

	/**
	 * Получить свободный ID
	 */
	public long getFreeId() {
		while (byId(currentId) != null || byDieId(currentId) != null)
			if (++currentId < 0)
				currentId = 1;
		return currentId;
	}

	/**
	 * Получить дракона из удалённых
	 */
	public Dragon byDieId(long id) { try{for (var e:collectionDie)if (e.getId()==id)return e;return null;} catch (IndexOutOfBoundsException e) { return null; } }

	/**
	 * Добавляет дракона по ID из удалённых
	 */
	public boolean add(long id) {
		Dragon ret = byDieId(id);
		if (ret == null) return false;
		dragons.put(ret.getId(), ret);
		collection.add(ret);
		collectionDie.remove(ret);
		update();
		return true;
	}

	/**
	 * Добавляет дракона
	 */
	public boolean add(Dragon d) {
		if (d == null) return false;
		dragons.put(d.getId(), d);
		collection.add(d);
		update();
		return true;
	}

	/**
	 * @return true в случае успеха.
	 */
	public boolean swap(long id, long repId) {
		var e = byId(id);
		var re = byId(repId);
		if (e == null) return false;
		if (re == null) return false;
		var ind = collection.indexOf(e);
		var rind = collection.indexOf(re);
		if (ind < 0) return false;
		if (rind < 0) return false;
		
		e.setId(repId);
		re.setId(id);
		
		dragons.put(e.getId(), e);
		dragons.put(re.getId(), re);
		
		// addLog("swap " + id + " " + repId , false);
		// replacement
		update();
		return true;
	}

	/**
	 * Удаляет дракона по ID
	 */
	public boolean remove(long id) {
		Dragon ret = byId(id);
		if (ret == null) return false;
		var ind = collection.indexOf(ret);
		if (ind < 0) return false;
		collection.remove(ret);
		collectionDie.add(ret);
		update();
		return true;
	}

	/**
	 * Отменяет n команд
	 */
	public int undo(int count) {
		var tmpLog = new LinkedList<String>();
		try {
			for (var i = 0 ; i < count ; i++) {
				if (logStack.size() == 0) return i;
				for (var j = logStack.size() - 1 ; j>=0 ; j--) {
					var s = logStack.pop();
					if (s.equals("+")) break;
					long n = -1;
					long n2 = -1;
					try { n = Long.parseLong((s + " ").split(" ")[1]); } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) { }
					try { n2 = Long.parseLong((s + " ").split(" ")[2]); } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) { }
					switch ((s + " ").split(" ")[0]) {
						case "add":
							if (!remove(n)) return i;
							tmpLog.push("remove " + n);
							break;
						case "remove":
							if (!add(n)) return i;
							tmpLog.push("add " + n);
							break;
						case "reorder":
							tmpLog.push("reorder");
							isAscendingSort ^= true;
							update();
							break;
						case "swap":
							if (!swap(n, n2)) return i;
							tmpLog.push("swap " + n + " " + n2);
							break;
						default:
							System.err.println("Undef command: " + s);
							System.out.println("Undef command: " + s);
							System.exit(1);
							return i;
					}
				}
			}
		} finally {
			addLog("", true);
			while (tmpLog.size()>0)
				addLog(tmpLog.removeLast(), false);
		}
		return count;
	}

public void deb2(long t) {
	System.out.println("    ===" + t+ "===");
	System.out.println(this);
	for (var ee : logStack)
		System.out.println("    "+t+"_" + ee);
	System.out.println("    ======");
	
}

	/**
	 * Создает транзакцию или добавляет операцию в транзакцию
	 */
	public void addLog(String cmd, boolean isFirst) {
		if (isFirst)
			logStack.push("+");
		if (!cmd.equals(""))
			logStack.push(cmd);
	}

	/**
	 * @return true в случае успеха.
	 */
	public boolean lastAddDragon(Dragon e) {
		if (isСontain(e)) return false;
		dragons.put(e.getId(), e);
		collection.add(e);
		return true;
	}

	/**
	 * Фиксирует изменения коллекции
	 */
	public void update() {
		Collections.sort(collection);
		if (isAscendingSort) Collections.reverse(collection);
	}

	/**
	 * Загружает коллекцию из файла.
	 * @return true в случае успеха.
	 */
	public boolean loadCollection() {
		dragons.clear();
		dumpManager.readCollection(collection, collectionDie, logStack);
		lastInitTime = LocalDateTime.now();
		for (var e : collection)
			if (byId(e.getId()) != null) {
				collection.clear();
				return false;
			} else {
				if (e.getId()>currentId) currentId = e.getId();
				dragons.put(e.getId(), e);
			}
		update();
		return true;
	}

	@Override
	public String toString() {
		if (collection.isEmpty()) return "Коллекция пуста!";

		StringBuilder info = new StringBuilder();
		for (Dragon dragon : collection) {
			info.append(dragon+"\n\n");
		}
		return info.toString().trim();
	}
}
