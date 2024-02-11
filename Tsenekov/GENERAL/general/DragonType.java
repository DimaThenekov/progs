package general;

import java.io.Serializable;

/**
 * Перечисление типов драконов.
 * @author dim0n4eg
 */
public enum DragonType implements Serializable {
	UNDERGROUND,
	AIR,
	FIRE;

	/**
	 * @return Строка со всеми элементами enum'а через запятую.
	 */
	public static String names() {
		StringBuilder nameList = new StringBuilder();
		for (var dragonType : values()) {
			nameList.append(dragonType.name()).append(", ");
		}
		return nameList.substring(0, nameList.length()-2);
	}
}
