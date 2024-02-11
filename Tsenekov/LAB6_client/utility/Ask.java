package utility;

import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import general.*;
import utility.Console;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;

/**
 * Класс чтения объекта
 * @author dim0n4eg
 */
public class Ask {
	public static class AskBreak extends Exception {}
	
	public static Dragon askDragon(Console console) throws AskBreak {
		/*
			private String name; //Поле не может быть null, Строка не может быть пустой
			private Coordinates coordinates; //Поле не может быть null
			private Integer age; //Значение поля должно быть больше 0, Поле может быть null
			private Color color; //Поле не может быть null
			private DragonType type; //Поле не может быть null
			private DragonCharacter character; //Поле может быть null
			private Person killer; //Поле может быть null
		*/
		try {
			console.print("name: ");
			String name;
			while (true) {
				name = console.readln().trim();
				if (name.equals("exit")) throw new AskBreak();
				if (!name.equals("")) break;
				console.print("name: ");
			}
			var coordinates = askCoordinates(console);
			console.print("age: ");
			Integer age;
			while (true) {
				var line = console.readln().trim();
				if (line.equals("exit")) throw new AskBreak();
				if (line.equals("")) { age = null; break;}
				try { age = Integer.parseInt(line); if (age>0) break; } catch (NumberFormatException e) { }
				console.print("age: ");
			}
			var color = askColor(console);
			var type = askDragonType(console);
			var character = askDragonCharacter(console, true);
			var killer = askPerson(console);
			return new Dragon(10000l, name, coordinates, age, color, type, character, killer);
		} catch (NoSuchElementException | IllegalStateException e) {
			console.printError("Ошибка чтения");
			return null;
		}
	}

	public static Coordinates askCoordinates(Console console) throws AskBreak {
		try {
			// private Integer x; //Значение поля должно быть больше -485, Поле не может быть null
			// private Double y; //Максимальное значение поля: 907, Поле не может быть null
			console.print("coordinates.x: ");
			Integer x;
			while (true) {
				var line = console.readln().trim();
				if (line.equals("exit")) throw new AskBreak();
				if (!line.equals("")) {
					try { x = Integer.parseInt(line); if (x>-485) break; } catch (NumberFormatException e) { }
				}
				console.print("coordinates.x: ");
			}
			console.print("coordinates.y: ");
			Double y;
			while (true) {
				var line = console.readln().trim();
				if (line.equals("exit")) throw new AskBreak();
				if (!line.equals("")) {
					try { y = Double.parseDouble(line); if (y<=907) break; } catch (NumberFormatException e) { }
				}
				console.print("coordinates.y: ");
			}
			
			return new Coordinates(x, y);
		} catch (NoSuchElementException | IllegalStateException e) {
			console.printError("Ошибка чтения");
			return null;
		}
	}

	public static Color askColor(Console console) throws AskBreak {
		try {
			console.print("Color ("+Color.names()+"): ");
			Color r;
			while (true) {
				var line = console.readln().trim();
				if (line.equals("exit")) throw new AskBreak();
				if (!line.equals("")) {
					try { r = Color.valueOf(line); break; } catch (NullPointerException | IllegalArgumentException  e) { }
				}
				console.print("Color: ");
			}
			return r;
		} catch (NoSuchElementException | IllegalStateException e) {
			console.printError("Ошибка чтения");
			return null;
		}
	}

	public static DragonType askDragonType(Console console) throws AskBreak {
		try {
			console.print("DragonType ("+DragonType.names()+"): ");
			DragonType r;
			while (true) {
				var line = console.readln().trim();
				if (line.equals("exit")) throw new AskBreak();
				if (!line.equals("")) {
					try { r = DragonType.valueOf(line); break; } catch (NullPointerException | IllegalArgumentException  e) { }
				}
				console.print("DragonType: ");
			}
			return r;
		} catch (NoSuchElementException | IllegalStateException e) {
			console.printError("Ошибка чтения");
			return null;
		}
	}

	public static DragonCharacter askDragonCharacter(Console console, boolean canNull) throws AskBreak {
		try {
			console.print("DragonCharacter ("+DragonCharacter.names()+"): ");
			DragonCharacter r;
			while (true) {
				var line = console.readln().trim();
				if (line.equals("exit")) throw new AskBreak();
				if (canNull && line.equals("")) return null;
				if (!line.equals("")) {
					try { r = DragonCharacter.valueOf(line); break; } catch (NullPointerException | IllegalArgumentException  e) { }
				}
				console.print("DragonCharacter: ");
			}
			return r;
		} catch (NoSuchElementException | IllegalStateException e) {
			console.printError("Ошибка чтения");
			return null;
		}
	}

	public static Person askPerson(Console console) throws AskBreak {
		try {
			/*
				private String name; //Поле не может быть null, Строка не может быть пустой
				private LocalDateTime birthday; //Поле может быть null
				private Long weight; //Поле может быть null, Значение поля должно быть больше 0
				private String passportID; //Длина строки должна быть не меньше 9, Поле может быть null
				private Color eyeColor; //Поле не может быть null
			*/
			console.print("person.name (null for Person:=null): ");
			String name;
			//while (true) {
			name = console.readln().trim();
			if (name.equals("exit")) throw new AskBreak();
			if (name.equals("")) return null;
			//	console.print("person.name: ");
			//}
			console.print("person.birthday (Exemple: "+LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)+" or 2023-03-11): ");
			LocalDateTime birthday;
			while (true) {
				var line = console.readln().trim();
				if (line.equals("exit")) throw new AskBreak();
				if (line.equals("")) { birthday = null; break;}
				try { birthday = LocalDateTime.parse(line, DateTimeFormatter.ISO_DATE_TIME); break; } catch (DateTimeParseException e) { }
				try { birthday = LocalDateTime.parse(line+"T00:00:00.0000", DateTimeFormatter.ISO_DATE_TIME); break; } catch (DateTimeParseException e) { }
				console.print("person.birthday: ");
			}
			
			
			console.print("person.weight: ");
			Long weight;
			while (true) {
				var line = console.readln().trim();
				if (line.equals("exit")) throw new AskBreak();
				if (line.equals("")) { weight = null; break;}
				try { weight = Long.parseLong(line); if (weight>0) break; } catch (NumberFormatException e) { }
				console.print("person.weight: ");
			}
			
			console.print("person.passportID: ");
			String passportID;
			while (true) {
				passportID = console.readln().trim();
				if (passportID.equals("exit")) throw new AskBreak();
				if (passportID.length() >= 9) break;
				console.print("person.passportID: ");
			}
			
			var eyeColor = askColor(console);
			
			return new Person(name, birthday, weight, passportID, eyeColor);
		} catch (NoSuchElementException | IllegalStateException e) {
			console.printError("Ошибка чтения");
			return null;
		}
	}
}
