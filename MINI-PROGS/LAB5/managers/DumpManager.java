package managers;

import au.com.bytecode.opencsv.*;

import java.io.StringWriter;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.lang.NullPointerException;
import java.util.Collection;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.LinkedList;
import utility.Console;
import models.Dragon;

/**
 * Использует файл для сохранения и загрузки коллекции.
 * @author dim0n4eg
 */
public class DumpManager {
	private final String fileName;
	private final Console console;

	public DumpManager(String fileName, Console console) {
		this.fileName = fileName;
		this.console = console;
	}

	/**
	 * Преобразует коллекцию в CSV-строку.
	 * @param коллекция
	 * @return CSV-строка
	 */
	private String collection2CSV(Collection<Dragon> collection) {
		try {
			StringWriter sw = new StringWriter();
			CSVWriter csvWriter = new CSVWriter(sw, ';');
			for (var e : collection) {
				csvWriter.writeNext(Dragon.toArray(e));
			}
			String csv = sw.toString();
			return csv;
		} catch (Exception e) {
			console.printError("Ошибка сериализации");
			return null;
		}
	}

	/**
	 * Записывает коллекцию в файл.
	 * @param collection коллекция
	 */
	public void writeCollection(Collection<Dragon> collection) {
		OutputStreamWriter writer = null, writer2 = null, writer3 = null;
		try {
			var csv = collection2CSV(collection);
			if (csv == null) return;
			writer = new OutputStreamWriter(new FileOutputStream(fileName));
			try {
				writer.write(csv);
				writer.flush();
				console.println("Коллекция успешна сохранена в файл!");
			} catch (IOException e) {
				console.printError("Неожиданная ошибка сохранения");
			}
		} catch (FileNotFoundException | NullPointerException e) {
			console.printError("Файл не найден");
		} finally {
			try {
				writer.close();
			} catch(IOException e) {
				console.printError("Ошибка закрытия файла");
			}
		}
	}

	/**
	 * Преобразует CSV-строку в коллекцию.
	 * @param CSV-строка
	 * @return коллекция
	 */
	private LinkedList<Dragon> CSV2collection(String s) {
		try {
			StringReader sr = new StringReader(s);
			CSVReader csvReader = new CSVReader(sr, ';');
			LinkedList<Dragon> ds = new LinkedList<Dragon>();
			String[] record = null;
			while ((record = csvReader.readNext()) != null) {
				Dragon d = Dragon.fromArray(record);
				if (d.validate())
					ds.add(d);
				else
					console.printError("Файл с колекцией содержит не действительные данные");
			}
			csvReader.close();
			return ds;
		} catch (Exception e) {
			console.printError("Ошибка десериализации");
			return null;
		}
	}

	/**
	 * Считывает коллекцию из файл.
	 * @return Считанная коллекция
	 */
	public void readCollection(Collection<Dragon> collection) {
		if (fileName != null && !fileName.isEmpty()) {
			try (var fileReader = new Scanner(new File(fileName))) {
				var s = new StringBuilder("");
				while (fileReader.hasNextLine()) {
					s.append(fileReader.nextLine());
					s.append("\n");
				}
				collection.clear();
				for (var e: CSV2collection(s.toString()))
					collection.add(e);
				if (collection != null) {
					console.println("Коллекция успешна загружена!");
					return;
				} else
					console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
			} catch (FileNotFoundException exception) {
				console.printError("Загрузочный файл не найден!");
			} catch (IllegalStateException exception) {
				console.printError("Непредвиденная ошибка!");
				System.exit(0);
			}
		} else {
			console.printError("Аргумент командной строки с загрузочным файлом не найден!");
		}
		collection = new LinkedList<Dragon>();
	}
}
