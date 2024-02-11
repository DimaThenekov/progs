package utility;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
import java.util.Scanner;

/**
 * Для ввода команд и вывода результата
 * @author dim0n4eg
 */
public class StandardConsole implements Console {
	private static final String P1 = "$ ";
	private static Scanner fileScanner = null;
	private static Scanner defScanner = new Scanner(System.in);

	/**
	 * Выводит obj.toString() в консоль
	 * @param obj Объект для печати
	 */
	public void print(Object obj) {
		System.out.print(obj);
	}

	/**
	 * Выводит obj.toString() + \n в консоль
	 * @param obj Объект для печати
	 */
	public void println(Object obj) {
		System.out.println(obj);
	}

	/**
	 * Выводит ошибка: obj.toString() в консоль
	 * @param obj Ошибка для печати
	 */
	public void printError(Object obj) {
		System.err.println("Error(err): " + obj);
		System.out.println("Error(out): " + obj);
	}

	public String readln() throws NoSuchElementException, IllegalStateException {
		return (fileScanner!=null?fileScanner:defScanner).nextLine();
	}

	public boolean isCanReadln() throws IllegalStateException {
		return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
	}

	/**
	 * Выводит таблицу из 2 колонок
	 * @param elementLeft Левый элемент колонки.
	 * @param elementRight Правый элемент колонки.
	 */
	public void printTable(Object elementLeft, Object elementRight) {
		System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
	}

	/**
	 * Выводит prompt1 текущей консоли
	 */
	public void prompt() {
		print(P1);
	}

	/**
	 * @return prompt1 текущей консоли
	 */
	public String getPrompt() {
		return P1;
	}

	public void selectFileScanner(Scanner scanner) {
		this.fileScanner = scanner;
	}

	public void selectConsoleScanner() {
		this.fileScanner = null;
	}
}
