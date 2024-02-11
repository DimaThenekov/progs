package general;

import general.Validatable;
import java.io.Serializable;

import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс человека
 * @author dim0n4eg
 */
public class Person implements Validatable, Serializable {
	private String name; //Поле не может быть null, Строка не может быть пустой
	private LocalDateTime birthday; //Поле может быть null
	private Long weight; //Поле может быть null, Значение поля должно быть больше 0
	private String passportID; //Длина строки должна быть не меньше 9, Поле может быть null
	private Color eyeColor; //Поле не может быть null

	public Person(String name, LocalDateTime birthday, Long weight, String passportID, Color eyeColor) {
		this.name = name;
		this.birthday = birthday;
		this.weight = weight;
		this.passportID = passportID;
		this.eyeColor = eyeColor;
	}

	public Person(String s) {
		try {
			this.name = s.split(" ; ")[0];
			try { this.birthday = (s.split(" ; ")[1].equals("null") ? null : LocalDateTime.parse(s.split(" ; ")[1], DateTimeFormatter.ISO_DATE_TIME)); } catch (DateTimeParseException e) { return; };
			try { this.weight = s.split(" ; ")[2].equals("null") ? null : Long.parseLong(s.split(" ; ")[2]); } catch (NumberFormatException e) { return; }
			try { this.passportID = s.split(" ; ")[3].equals("null") ? null : s.split(" ; ")[3]; } catch (NumberFormatException e) {  return; }
			try { this.eyeColor = Color.valueOf(s.split(" ; ")[4]); } catch (NullPointerException | IllegalArgumentException  e) { this.eyeColor = null; }
		} catch (ArrayIndexOutOfBoundsException e) {}
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getBirthday() {
		return birthday;
	}

	public Long getWeight() {
		return weight;
	}

	public String getPassportID() {
		return passportID;
	}

	public Color getEyeColor() {
		return eyeColor;
	}

	/**
	 * Валидирует правильность полей.
	 * @return true, если все верно, иначе false
	 */
	public boolean validate() {
		if (name == null || name.isEmpty()) return false;
		if (weight != null && weight<=0) return false;
		if (passportID != null && passportID.length() < 9) return false;
		if (eyeColor == null) return false;
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return Objects.equals(name, person.name) && Objects.equals(birthday, person.birthday)
			&& Objects.equals(weight, person.weight) && Objects.equals(passportID, person.passportID)
			&& Objects.equals(eyeColor, person.eyeColor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, birthday, weight, passportID, eyeColor);
	}

	@Override
	public String toString() {
		/*return "'name' = '" + name + "' ; " +
			"'birthday' = " + (birthday == null ? "null" : "'"+birthday.format(DateTimeFormatter.ISO_DATE_TIME)+"'") + " ; " +
			"'weight' = " + (weight == null ? "null" : "'"+weight+"'") + " ; " +
			"'passportID' = " + (passportID == null ? "null" : "'"+passportID+"'") + " ; " +
			"'color' = '" + eyeColor + "'";*/
		return name + " ; " + (birthday == null ? "null" : birthday.format(DateTimeFormatter.ISO_DATE_TIME)) + " ; " + (weight == null ? "null" : weight) + " ; " + (passportID == null ? "null" : passportID) + " ; " + eyeColor;
	}
}
