package general;

import java.io.Serializable;

/**
 * Перечисление цветов.
 * @author dim0n4eg
 */
public enum Color implements Serializable {
	GREEN,
	RED,
	BLACK,
	ORANGE;

	/**
	 * @return Строка со всеми элементами enum'а через запятую.
	 */
	public static String names() {
		StringBuilder nameList = new StringBuilder();
		for (var colorType : values()) {
			nameList.append(colorType.name()).append(", ");
		}
		return nameList.substring(0, nameList.length()-2);
	}
}
