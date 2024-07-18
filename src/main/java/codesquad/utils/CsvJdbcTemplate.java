package codesquad.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.db.csv.driver.CsvResultSet;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;

public class CsvJdbcTemplate {

	public static final String DB_URL = "jdbc:csv:./data";
	private static final Logger log = LoggerFactory.getLogger(CsvJdbcTemplate.class);

	private CsvJdbcTemplate() {
	}

	public static long update(String sql, QuerySetter qs) {
		try (Connection conn = DriverManager.getConnection(DB_URL);
			 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			if (qs != null) {
				qs.setValues(ps);
			}
			ps.executeUpdate();
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys == null) {
					return 0;
				}
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				}
			}
			return 0;
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw BaseException.serverException(e);
		}
	}

	public static <T> List<T> execute(String sql, Class<T> clazz, QuerySetter qs) {
		List<T> result = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(DB_URL);
			 PreparedStatement ps = getPreparedStatement(conn, sql, qs);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				result.add(createInstanceFromResultSet(clazz, rs));
			}
		} catch (SQLException e) {
			log.error("Query execution error: {}", sql, e);
			throw BaseException.serverException(e);
		}
		return result;
	}

	public static <T> T executeOne(String sql, Class<T> clazz, QuerySetter qs) {
		try (Connection conn = DriverManager.getConnection(DB_URL);
			 PreparedStatement ps = getPreparedStatement(conn, sql, qs);
			 ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return createInstanceFromResultSet(clazz, rs);
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw BaseException.serverException(e);
		}
		return null;
	}

	private static PreparedStatement getPreparedStatement(Connection conn, String sql, QuerySetter qs)
		throws SQLException {
		PreparedStatement ps = conn.prepareStatement(sql);
		if (qs != null) {
			qs.setValues(ps);
		}
		return ps;
	}

	private static <T> T createInstanceFromResultSet(Class<T> clazz, ResultSet rs) {
		Constructor<?> constructor = getConstructor(clazz);
		Object[] parameters;
		try {
			parameters = getClassParameters(clazz, rs);
			return (T)constructor.newInstance(parameters);
		} catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			log.error(e.getMessage());
			throw BaseException.serverException(e);
		}
	}

	private static <T> Object[] getClassParameters(Class<T> clazz, ResultSet rs) throws SQLException {
		Field[] fields = clazz.getDeclaredFields();
		Object[] parameters = new Object[fields.length];
		Class<?>[] types = null;
		if (rs instanceof CsvResultSet) {
			types = ((CsvResultSet)rs).getTypes();
		}
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			if (types == null) {
				Object value = rs.getObject(fieldName);
				if (value instanceof Blob blob) {
					parameters[i] = blob.getBytes(1L, (int)blob.length());
				} else {
					parameters[i] = value;
				}
			} else {
				if (types[i] == String.class) {
					parameters[i] = rs.getString(fieldName);
				} else if (types[i] == byte[].class) {
					parameters[i] = rs.getBytes(fieldName);
				} else if (types[i] == Long.class) {
					parameters[i] = rs.getLong(fieldName);
				}
			}
		}
		return parameters;
	}

	public static Object[] extractParameters(ResultSet rs, Field[] fields) throws SQLException {
		Object[] parameters = new Object[fields.length];

		Class<?>[] types = null;
		if (rs instanceof CsvResultSet) {
			types = ((CsvResultSet)rs).getTypes();
		}

		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			if (types[i] == Blob.class) {
				parameters[i] = rs.getBytes(2);
			} else if (fields[i].getType() == String.class) {
				parameters[i] = rs.getString(fieldName);
			} else if (fields[i].getType() == Long.class) {
				parameters[i] = rs.getLong(fieldName);
			} else if (fields[i].getType() == Integer.class) {
				parameters[i] = rs.getInt(fieldName);
			} else if (fields[i].getType() == byte[].class) {
				parameters[i] = rs.getBytes(fieldName);
			}
		}

		return parameters;
	}

	private static <T> Constructor<?> getConstructor(Class<T> clazz) {
		for (Constructor<?> ctor : clazz.getConstructors()) {
			if (ctor.getParameterCount() == clazz.getDeclaredFields().length) {
				ctor.setAccessible(true);
				return ctor;
			}
		}
		throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
	}
}
