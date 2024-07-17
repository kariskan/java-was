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
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;

public class JdbcTemplate {

	public static final String H2_DRIVER = "org.h2.Driver";
	public static final String DB_URL = "jdbc:h2:tcp://localhost/~/codestagram";
	public static final String USER = "sa";
	public static final String PASS = "";
	private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

	private JdbcTemplate() {
	}

	public static long update(String sql, List<Pair<SQLType, Object>> params) {
		try (
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			PreparedStatement ps = getPreparedStatement(conn, sql, params)
		) {
			ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			}
			return 0;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
		}
	}

	public static <T> List<T> execute(String sql, Class<T> clazz, List<Pair<SQLType, Object>> params) {
		List<T> result = new ArrayList<>();
		try (
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			PreparedStatement ps = getPreparedStatement(conn, sql, params);
			ResultSet rs = ps.executeQuery()
		) {
			while (rs.next()) {
				Constructor<?> constructor = getConstructor(clazz);
				Object[] parameters = getClassParameters(clazz, rs);
				Object o = constructor.newInstance(parameters);
				result.add((T)o);
			}
		} catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			log.error(e.getMessage(), e);
			throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
		}
		return result;
	}

	public static <T> T executeOne(String sql, Class<T> clazz, List<Pair<SQLType, Object>> params) {
		List<T> result = new ArrayList<>();
		try (
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			PreparedStatement ps = getPreparedStatement(conn, sql, params);
			ResultSet rs = ps.executeQuery()
		) {
			while (rs.next()) {
				Constructor<?> constructor = getConstructor(clazz);
				Object[] parameters = getClassParameters(clazz, rs);
				Object o = constructor.newInstance(parameters);
				result.add((T)o);
			}
		} catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			log.error(e.getMessage(), e);
			throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
		}
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	private static <T> Object[] getClassParameters(Class<T> clazz, ResultSet rs) throws SQLException {
		Field[] fields = clazz.getDeclaredFields();
		Object[] parameters = new Object[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			Object value = rs.getObject(fieldName);
			if (value instanceof Blob blob) {
				parameters[i] = blob.getBytes(1L, (int) blob.length());
			} else {
				parameters[i] = value;
			}
		}
		return parameters;
	}

	private static <T> Constructor<?> getConstructor(Class<T> clazz) {
		Constructor<?> constructor = null;
		Constructor<?>[] constructors = clazz.getConstructors();
		for (Constructor<?> ctor : constructors) {
			if (ctor.getParameterCount() == clazz.getDeclaredFields().length) {
				constructor = ctor;
				break;
			}
		}
		if (constructor != null) {
			constructor.setAccessible(true);
		}
		return constructor;
	}

	public static PreparedStatement getPreparedStatement(Connection conn, String sql,
		List<Pair<SQLType, Object>> params) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		if (params == null) {
			return ps;
		}
		int idx = 1;
		for (Pair<SQLType, Object> pair : params) {
			SQLType sqlType = pair.getLeft();
			Object value = pair.getRight();
			ps.setObject(idx++, value, sqlType);
		}
		return ps;
	}
}
