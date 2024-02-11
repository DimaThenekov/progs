package managers;

import au.com.bytecode.opencsv.*;

import java.io.StringWriter;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.NullPointerException;
import java.util.Collection;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.LinkedList;
import utility.Console;
import general.Dragon;

import java.util.Properties;

/**
 * Использует файл для сохранения и загрузки коллекции.
 * @author dim0n4eg
 */
public class DumpManager {
	private final String fileName;
	private final Console console;
	private final Properties properties = new Properties();

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
	public void writeCollection(Collection<Dragon> collection, Collection<Dragon> collectionDie, ArrayDeque<String> logStack) {
		OutputStreamWriter writer = null, writer2 = null, writer3 = null;
		try {
			var csv = collection2CSV(collection);
			if (csv == null) return;
			writer = new OutputStreamWriter(new FileOutputStream(fileName));
			var csv2 = collection2CSV(collectionDie);
			if (csv2 == null) return;
			writer2 = new OutputStreamWriter(new FileOutputStream(fileName+"_die.txt"));
			writer3 = new OutputStreamWriter(new FileOutputStream(fileName+"_log.txt"));
			try {
				writer.write(csv);
				writer.flush();
				writer2.write(csv2);
				writer2.flush();
				for (var line : logStack)
					writer3.write(line+"\r\n");
				writer3.flush();
				console.println("Коллекция успешна сохранена в файл!");
			} catch (IOException e) {
				console.printError("Неожиданная ошибка сохранения");
			}
		} catch (FileNotFoundException | NullPointerException e) {
			console.printError("Файл не найден");
		} finally {
			try {
				writer.close();
				writer2.close();
				writer3.close();
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
	public void readCollection(Collection<Dragon> collection, Collection<Dragon> collectionDie, ArrayDeque<String> logStack) {
		try {
			if (!(new File(fileName+"_cnf.properties")).exists()) {
				var writer4 = new OutputStreamWriter(new FileOutputStream(fileName+"_cnf.properties"));
				writer4.write("PORT:23637\r\n");
				writer4.flush();
				writer4.close();
			}
		} catch (IOException e) {
			console.printError("Неожиданная ошибка сохранения");
		}
		if (fileName != null && !fileName.isEmpty()) {
			try (var fileReader = new Scanner(new File(fileName));
				var fileReader2 = new Scanner(new File(fileName+"_die.txt"));
				var fileReader3 = new Scanner(new File(fileName+"_log.txt"));
				var fileReader4 = new Scanner(new File(fileName+"_cnf.properties"))) {
				var s = new StringBuilder("");
				while (fileReader.hasNextLine()) {
					s.append(fileReader.nextLine());
					s.append("\n");
				}
				var s2 = new StringBuilder("");
				while (fileReader2.hasNextLine()) {
					s2.append(fileReader2.nextLine());
					s2.append("\n");
				}
				var tmpStack = new ArrayDeque<String>();
				while (fileReader3.hasNextLine()) {
					tmpStack.push(fileReader3.nextLine());
				}
				var s3 = new StringBuilder("");
				while (fileReader4.hasNextLine()) {
					s3.append(fileReader4.nextLine());
					s3.append("\n");
				}
				properties.load(new ByteArrayInputStream(s3.toString().getBytes()));
				//System.out.println(properties.getProperty("PORT"));
				for (var e : tmpStack)
					logStack.push(e);
				collection.clear();
				for (var e: CSV2collection(s.toString()))
					collection.add(e);
				collectionDie.clear();
				for (var e: CSV2collection(s2.toString()))
					collectionDie.add(e);
				if (collection != null && collectionDie != null) {
					console.println("Коллекция успешна загружена!");
					return;
				} else
					console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
			} catch (FileNotFoundException exception) {
				console.printError("Загрузочный файл не найден!");
			} catch (IOException e) {
				console.printError("Ошибка чтения");
			} catch (IllegalStateException exception) {
				console.printError("Непредвиденная ошибка!");
				System.exit(0);
			}
		} else {
			console.printError("Аргумент командной строки с загрузочным файлом не найден!");
		}
		collection = new LinkedList<Dragon>();
	}
	
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	public int getProperty(String key, int defaultValue) {
		try {
			return Integer.parseInt(properties.getProperty(key, ""));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
