package managers;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Stack;
import general.Dragon;
import java.time.LocalDateTime;
import java.util.*;
import utility.User;

/**
 * Оперирует коллекцией.
 * @author dim0n4eg
 */
public class CollectionManager {
	private ConcurrentLinkedDeque<Dragon> collection = new ConcurrentLinkedDeque<Dragon>();
	private ConcurrentHashMap<Dragon, Long> userIdMap = new ConcurrentHashMap<>();
	private LocalDateTime lastInitTime;
	private LocalDateTime lastSaveTime;
	private final DumpManager dumpManager;
	private final UserManager userManager;
	public boolean isAscendingSort;

	public CollectionManager(DumpManager dumpManager, UserManager userManager) {
		this.lastInitTime = null;
		this.lastSaveTime = null;
		this.dumpManager = dumpManager;
		this.userManager = userManager;
	}

	/**
	 * @return коллекция.
	 */
	public ConcurrentLinkedDeque<Dragon> getCollection() {
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
	 * Получить дракона по ID
	 */
	public Dragon byId(Long id) { for (var e: collection.toArray()) if (((Dragon)e).getId()==id) return (Dragon)e; return null; }

	/**
	 * Содержит ли колекции дракона
	 */
	public boolean isСontain(Dragon e) { return e == null || byId(e.getId()) != null; }

	/**
	 * Добавляет дракона
	 */
	public boolean add(Dragon d, User u) {
		if (d == null || u == null) return false;
		if (!dumpManager.insertD(d, u.getID())) return false;
		collection.addLast(d);
		userIdMap.put(d, u.getID());
		update();
		return true;
	}

	/**
	 * Обновляет дракона по ID
	 */
	public boolean update(Dragon d, User u) {
		if (d == null || u == null) return false;
		var oldD = byId(d.getId());
		if (oldD == null) return false;
		if (!(userManager.canEval(u, "REMOVEALL") || userIdMap.get(oldD)==u.getID())) return false;
		if (!dumpManager.updateD(d)) return false;
		collection.remove(oldD);
		collection.addLast(d);
		userIdMap.put(d, u.getID());
		update();
		return true;
	}

	/**
	 * Удаляет дракона по ID
	 */
	public boolean remove(long id, User u) {
		if (u == null) return false;
		var d = byId(id);
		if (d == null) return false;
		if (!(userManager.canEval(u, "REMOVEALL") || userIdMap.get(d)==u.getID())) return false;
		if (!dumpManager.removeD(id)) return false;
		collection.remove(d);
		update();
		return true;
	}

	/**
	 * Фиксирует изменения коллекции
	 */
	public void update() {
		Dragon[] a = collection.toArray(new Dragon[0]);
		Arrays.sort(a);
		collection.clear();
		for (Dragon card : a) {
			if (!isAscendingSort)
				collection.addLast(card);
			else
				collection.addFirst(card);
		}
	}

	public void init() {
		collection.clear();
		userIdMap.clear();
		for (var e: dumpManager.selectD()) {
			collection.addLast(e.d);
			userIdMap.put(e.d, e.u);
		}
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
