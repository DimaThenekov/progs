package general;

import java.io.Serializable;

/**
 * Перечисление типов характера драконов.
 * @author dim0n4eg
 */
public enum DragonCharacter implements Serializable {
	CUNNING,
	EVIL,
	GOOD,
	CHAOTIC;

	/**
	 * @return Строка со всеми элементами enum'а через запятую.
	 */
	public static String names() {
		StringBuilder nameList = new StringBuilder();
		for (var dragonCharacterType : values()) {
			nameList.append(dragonCharacterType.name()).append(", ");
		}
		return nameList.substring(0, nameList.length()-2);
	}
}
