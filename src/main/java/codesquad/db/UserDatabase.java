package codesquad.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import codesquad.data.User;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;
import codesquad.utils.CsvJdbcTemplate;

public class UserDatabase implements Database<String, User> {

	private static final UserDatabase instance = new UserDatabase();
	private final Map<String, User> users = new ConcurrentHashMap<>();

	private UserDatabase() {
	}

	public static UserDatabase getInstance() {
		return instance;
	}

	@Override
	public Long insert(String id, User user) {
		String insert = """
			insert into USERS (userid, nickname, password, email)
			values (?, ?, ?, ?)
			""";
		return CsvJdbcTemplate.update(insert, ps -> {
			ps.setString(1, user.userId());
			ps.setString(2, user.nickname());
			ps.setString(3, user.password());
			ps.setString(4, user.email());
		});
	}

	@Override
	public User get(String id) {
		String findById = """
			select * from USERS where userid = ?
			""";
		return CsvJdbcTemplate.executeOne(findById, User.class, ps -> {
			try {
				ps.setString(1, id);
			} catch (SQLException e) {
				throw BaseException.serverException(e);
			}
		});
	}

	@Override
	public void update(String id, User user) {
		users.compute(id, (s, val) -> {
			if (val == null) {
				throw new BaseException(HttpStatus.BAD_REQUEST, "User with id " + id + " does not exist");
			}
			return user;
		});
	}

	@Override
	public void delete(String id) {
		users.remove(id);
	}

	public void clear() {
		users.clear();
	}

	@Override
	public List<User> findAll() {
		String select = """
			select * from USERS
			""";
		return CsvJdbcTemplate.execute(select, User.class, null);
	}
}
