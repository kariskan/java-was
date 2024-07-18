package codesquad.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import codesquad.error.BaseException;
import codesquad.utils.CsvParser.Column;
import codesquad.utils.CsvParser.Table;

public class CsvUtil {

	private CsvUtil() {
	}

	public static List<String[]> readCsv(String filePath) {
		List<String[]> records = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream("data/" + filePath + ".csv");
			 InputStreamReader isr = new InputStreamReader(fis);
			 BufferedReader reader = new BufferedReader(isr)) {
			String line;
			while ((line = reader.readLine()) != null) {
				records.add(line.split(","));
			}
		} catch (IOException e) {
			throw BaseException.serverException(e);
		}
		return records;
	}

	public static String[] readCsvOne(String filePath, int idx) {
		List<String[]> records = readCsv(filePath);
		return records.get(idx);
	}

	public static void writeCsv(String filePath, List<String[]> data) {
		try (FileOutputStream fos = new FileOutputStream("data/" + filePath + ".csv");
			 OutputStreamWriter osw = new OutputStreamWriter(fos);
			 BufferedWriter writer = new BufferedWriter(osw)) {
			for (String[] record : data) {
				writer.write(String.join(",", record));
				writer.newLine();
			}
		} catch (IOException e) {
			throw BaseException.serverException(e);
		}
	}

	public static void generateCsvFiles(Table table) {
		String filePath = "data/" + table.name() + ".csv";
		try (FileWriter writer = new FileWriter(filePath)) {
			StringBuilder header = new StringBuilder();
			for (Column column : table.columns()) {
				header.append(column.name()).append(",");
			}
			writer.append(header.substring(0, header.length() - 1)).append("\n");
		} catch (IOException e) {
			throw BaseException.serverException(e);
		}
	}

	public static void removeCsvFile(String name) {
		File file = new File("data/" + name + ".csv");
		if (!file.exists()) {
			return;
		}
		if (!file.delete()) {
			throw BaseException.serverException();
		}
	}
}
