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

import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;

public class JdbcTemplate {

	public static final String DB_URL = "jdbc:h2:tcp://localhost/~/codestagram";
	public static final String USER = "sa";
	public static final String PASS = "";
	private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

	private JdbcTemplate() {
	}

	public static long update(String sql, QuerySetter qs) {
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			if (qs != null) {
				qs.setValues(ps);
			}
			ps.executeUpdate();
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
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
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
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
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
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

	private static PreparedStatement getPreparedStatement(Connection conn, String sql, QuerySetter qs) throws
		SQLException {
		PreparedStatement ps = conn.prepareStatement(sql);
		if (qs != null) {
			qs.setValues(ps);
		}
		return ps;
	}

	private static <T> T createInstanceFromResultSet(Class<T> clazz, ResultSet rs)  {
		Constructor<?> constructor = getConstructor(clazz);
		Object[] parameters;
		try {
			parameters = getClassParameters(clazz, rs);
			return (T)constructor.newInstance(parameters);
		} catch (SQLException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			log.error(e.getMessage());
			throw BaseException.serverException(e);
		}
	}

	private static <T> Object[] getClassParameters(Class<T> clazz, ResultSet rs) throws SQLException {
		Field[] fields = clazz.getDeclaredFields();
		Object[] parameters = new Object[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			Object value = rs.getObject(fieldName);
			if (value instanceof Blob blob) {
				parameters[i] = blob.getBytes(1L, (int)blob.length());
			} else {
				parameters[i] = value;
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