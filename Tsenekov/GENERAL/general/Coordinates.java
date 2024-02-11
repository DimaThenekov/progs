package general;

import general.Validatable;
import java.io.Serializable;

/**
 * Классс координат.
 * @author dim0n4eg
 */
public class Coordinates implements Validatable, Serializable {
	private Integer x; //Значение поля должно быть больше -485, Поле не может быть null
	private Double y; //Максимальное значение поля: 907, Поле не может быть null

	public Coordinates (Integer x, Double y) {
		this.x = x;
		this.y = y;
	}

	public Coordinates(String s) {
		try {
			try { this.x = Integer.parseInt(s.split(";")[0]); } catch (NumberFormatException e) { }
			try { this.y = Double.parseDouble(s.split(";")[1]); } catch (NumberFormatException e) { }
		} catch (ArrayIndexOutOfBoundsException e) {}
	}

	public Integer getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	/**
	 * Валидирует правильность полей.
	 * @return true, если все верно, иначе false
	 */
	@Override
	public boolean validate() {
		if (x == null || y == null) return false;
		return x>-485 && y<=907;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Coordinates that = (Coordinates) obj;
		return x.equals(that.x) && y.equals(that.y);
	}

	@Override
	public int hashCode() {
		return x.hashCode() + y.hashCode();
	}

	@Override
	public String toString() {
		return x + ";" + y;
	}
}
