package general;

import general.Element;
import general.Validatable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.*;
import java.io.Serializable;

/**
 * Класс драконов
 * @author dim0n4eg
 */
public class Dragon extends Element implements Validatable, Serializable {
	private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
	private String name; //Поле не может быть null, Строка не может быть пустой
	private Coordinates coordinates; //Поле не может быть null
	private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
	private Integer age; //Значение поля должно быть больше 0, Поле может быть null
	private Color color; //Поле не может быть null
	private DragonType type; //Поле не может быть null
	private DragonCharacter character; //Поле может быть null
	private Person killer; //Поле может быть null

	public Dragon(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, Integer age, Color color, DragonType type, DragonCharacter character, Person killer) {
		this.id = id;
		this.name = name;
		this.coordinates = coordinates;
		this.creationDate = creationDate;
		this.age = age;
		this.color = color;
		this.type = type;
		this.character = character;
		this.killer = killer;
	}

	public Dragon(Long id, String name, Coordinates coordinates, Integer age, Color color, DragonType type, DragonCharacter character, Person killer) {
		this(id, name, coordinates, LocalDateTime.now(), age, color, type, character, killer);
	}

	/**
	 * Валидирует правильность полей.
	 * @return true, если все верно, иначе false
	 */
	@Override
	public boolean validate() {
		if (id == null || id <= 0) return false;
		if (name == null || name.isEmpty()) return false;
		if (coordinates == null || !coordinates.validate()) return false;
		if (creationDate == null) return false;
		if (age != null && age <= 0) return false;
		if (color == null) return false;
		if (type == null) return false;
		if (killer != null && !killer.validate()) return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public Integer getAge() {
		return age;
	}

	public Color getColor() {
		return color;
	}

	public DragonType getType() {
		return type;
	}

	public DragonCharacter getCharacter() {
		return character;
	}

	public Person getKiller() {
		return killer;
	}

	public static Dragon fromArray(String[] a) {
		Long id;
		String name;
		Coordinates coordinates;
		LocalDateTime creationDate;
		Integer age;
		Color color;
		DragonType type;
		DragonCharacter character;
		Person killer;
		try {
			try { id = Long.parseLong(a[0]); } catch (NumberFormatException e) { id = null; }
			name = a[1];
			coordinates = new Coordinates(a[2]);
			try { creationDate = LocalDateTime.parse(a[3], DateTimeFormatter.ISO_DATE_TIME); } catch (DateTimeParseException e) { creationDate = null; };
			try { age = (a[4].equals("null") ? null : Integer.parseInt(a[4])); } catch (NumberFormatException e) { age = null; }
			try { color = Color.valueOf(a[5]); } catch (NullPointerException | IllegalArgumentException  e) { color = null; }
			try { type = DragonType.valueOf(a[6]); } catch (NullPointerException | IllegalArgumentException  e) { type = null; }
			try { character = (a[7].equals("null") ? null : DragonCharacter.valueOf(a[7])); } catch (NullPointerException | IllegalArgumentException  e) { character = null; }
			killer = (a[8].equals("null") ? null : new Person(a[8]));
			return new Dragon(id, name, coordinates, creationDate, age, color, type, character, killer);
		} catch (ArrayIndexOutOfBoundsException e) {}
		return null;
	}

	public static String[] toArray(Dragon e) {
		var list = new ArrayList<String>();
		list.add(e.getId().toString());
		list.add(e.getName());
		list.add(e.getCoordinates().toString());
		list.add(e.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME));
		list.add(e.getAge() == null ? "null" : e.getAge().toString());
		list.add(e.getColor().toString());
		list.add(e.getType().toString());
		list.add(e.getCharacter() == null ? "null" : e.getCharacter().toString());
		list.add(e.getKiller() == null ? "null" : e.getKiller().toString());
		return list.toArray(new String[0]);
	}

	@Override
	public int compareTo(Element element) {
		return (int)(this.id - element.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Dragon that = (Dragon) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, coordinates, creationDate, age, color, type, character, killer);
	}

	@Override
	public String toString() {
		return "d{\"id\": " + id + ", " +
			"\"name\": \"" + name + "\", " +
			"\"coordinates\": \"" + coordinates + "\", " +
			"\"creationDate\" = \"" + creationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "\", " +
			"\"age\": " + (age == null ? "null" : "\""+age.toString()+"\"") + ", " +
			"\"color\" = \"" + color + "\", " +
			"\"type\": \"" + type + "\", " +
			"\"character\": " + (character == null ? "null" : "\""+character+"\"") + ", " +
			"\"killer\": " + (killer == null ? "null" : "\""+killer+"\"") +"}";
	}
}
