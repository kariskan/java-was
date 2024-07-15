package codesquad.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public static int update(String sql, Map<SQLType, Object> params) {
		try (
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			PreparedStatement ps = getPreparedStatement(conn, sql, params)
		) {
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
		}
	}

	public static <T> List<T> execute(String sql, Class<T> clazz, Map<SQLType, Object> params) {
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
			throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
		}
		return result;
	}

	private static <T> Object[] getClassParameters(Class<T> clazz, ResultSet rs) throws SQLException {
		Field[] fields = clazz.getDeclaredFields();
		Object[] parameters = new Object[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			Object value = rs.getObject(fieldName);
			parameters[i] = value;
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

	public static PreparedStatement getPreparedStatement(Connection conn, String sql, Map<SQLType, Object> params)
		throws SQLException {
		PreparedStatement ps = conn.prepareStatement(sql);
		if (params == null) {
			return ps;
		}
		for (Map.Entry<SQLType, Object> entry : params.entrySet()) {
			SQLType sqlType = entry.getKey();
			Object value = entry.getValue();
			ps.setObject(1, value, sqlType);
		}
		return ps;
	}
}
