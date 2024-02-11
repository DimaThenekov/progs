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
	private LinkedList<Dragon> collection = new LinkedList<Dragon>();
	private LocalDateTime lastInitTime;
	private LocalDateTime lastSaveTime;
	private final DumpManager dumpManager;

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
		dumpManager.writeCollection(collection);
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
		while (byId(currentId) != null)
			if (++currentId < 0)
				currentId = 1;
		return currentId;
	}


	/**
	 * Добавляет дракона
	 */
	public boolean add(Dragon d) {
		if (isСontain(d)) return false;
		dragons.put(d.getId(), d);
		collection.add(d);
		update();
		return true;
	}

    /**
     * Удаляет Aboba по ID
     */
    public boolean remove(long id) {
        var a = byId(id);
        if (a == null) return false;
        dragons.remove(a.getId());
        collection.remove(a);
        update();
        return true;
    }

	/**
	 * Фиксирует изменения коллекции
	 */
	public void update() {
		Collections.sort(collection);
	}
	
	/**
	 * Загружает коллекцию из файла.
	 * @return true в случае успеха.
	 */
	public boolean loadCollection() {
		dragons.clear();
		dumpManager.readCollection(collection);
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
