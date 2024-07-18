package codesquad.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvParser {

	private CsvParser() {
	}

	public static List<Table> parseCreateTableStatements(String sql) {
		List<Table> tables = new ArrayList<>();
		Pattern pattern = Pattern.compile(
			"create table (\\w+)\\s*\\(([^;]+)\\);",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
		);
		Matcher matcher = pattern.matcher(sql);

		while (matcher.find()) {
			String tableName = matcher.group(1);
			String columnsPart = matcher.group(2);
			List<Column> columns = parseColumns(columnsPart);
			tables.add(new Table(tableName, columns));
		}
		return tables;
	}

	public static InsertStatement parseInsertStatement(String sql) {
		Pattern pattern = Pattern.compile(
			"insert into (\\w+) \\(([^)]+)\\) values \\(([^)]+)\\);?",
			Pattern.CASE_INSENSITIVE
		);
		Matcher matcher = pattern.matcher(sql);
		if (matcher.find()) {
			String tableName = matcher.group(1);
			String[] columns = matcher.group(2).split("\\s*,\\s*");
			String[] values = matcher.group(3).split("\\s*,\\s*");
			return new InsertStatement(tableName, columns, values);
		}
		throw new IllegalArgumentException("Invalid INSERT statement: " + sql);
	}

	public static SelectStatement parseSelectStatement(String sql) {
		Pattern pattern = Pattern.compile(
			"select (.+) from (\\w+)( where (.+))?",
			Pattern.CASE_INSENSITIVE
		);
		Matcher matcher = pattern.matcher(sql);
		if (matcher.find()) {
			String[] columns = matcher.group(1).split("\\s*,\\s*");
			String tableName = matcher.group(2);
			String condition = matcher.group(4);
			return new SelectStatement(tableName, columns, condition);
		}
		throw new IllegalArgumentException("Invalid SELECT statement: " + sql);
	}

	private static List<Column> parseColumns(String columnsPart) {
		List<Column> columns = new ArrayList<>();
		Pattern columnPattern = Pattern.compile(
			"(\\w+)\\s+(\\w+)(?:\\((\\d+)\\))?(.*?),",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
		);
		Matcher matcher = columnPattern.matcher(columnsPart + ",");

		while (matcher.find()) {
			String columnName = matcher.group(1);
			String dataType = matcher.group(2);
			String length = matcher.group(3);
			boolean isPrimaryKey = matcher.group(4).toLowerCase().contains("primary key");
			columns.add(new Column(columnName, dataType, length, isPrimaryKey));
		}
		return columns;
	}

	public record Table(String name, List<Column> columns) {
	}

	public record Column(String name, String type, String length, boolean isPrimaryKey) {
	}

	public record InsertStatement(String tableName, String[] columns, String[] values) {
	}

	public record SelectStatement(String tableName, String[] columns, String condition) {
	}
}