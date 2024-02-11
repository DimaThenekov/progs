package utility;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
import java.util.Scanner;

/**
 * Для ввода команд и вывода результата
 * @author dim0n4eg
 */
public class ClientConsole implements Console {
	private static final String P1 = "$ ";
	private static StringBuilder out = new StringBuilder();

	/**
	 * Выводит obj.toString() в консоль
	 * @param obj Объект для печати
	 */
	public void print(Object obj) {
		out.append(obj==null?"null":obj.toString());
	}

	/**
	 * Выводит obj.toString() + \n в консоль
	 * @param obj Объект для печати
	 */
	public void println(Object obj) {
		out.append(obj==null?"null\n":obj.toString()+"\n");
	}

	/**
	 * Выводит ошибка: obj.toString() в консоль
	 * @param obj Ошибка для печати
	 */
	public void printError(Object obj) {
		out.append("Error(err):"+(obj==null?"null\n":obj.toString()+"\n"));
	}

	public String readln() throws NoSuchElementException, IllegalStateException { return null; }

	public boolean isCanReadln() throws IllegalStateException { return false; }

	/**
	 * Выводит таблицу из 2 колонок
	 * @param elementLeft Левый элемент колонки.
	 * @param elementRight Правый элемент колонки.
	 */
	public void printTable(Object elementLeft, Object elementRight) {
		 out.append(String.format(" %-35s%-1s%n", elementLeft, elementRight));
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

	public void selectFileScanner(Scanner scanner) {}

	public void selectConsoleScanner() {}
	
	public void clearOut() {out.setLength(0);}
	public String getOut() {return out.toString();}
}
