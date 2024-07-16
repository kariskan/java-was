package codesquad.db;

import static codesquad.utils.Pair.of;

import codesquad.data.User;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;
import codesquad.utils.JdbcTemplate;
import codesquad.utils.Pair;
import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDatabase implements Database<String, User> {

	private static final UserDatabase instance = new UserDatabase();
	private final Map<String, User> users = new ConcurrentHashMap<>();

	private UserDatabase() {
	}

	public static UserDatabase getInstance() {
		return instance;
	}

	@Override
	public void insert(String id, User user) {
		String insert = """
			insert into USERS(userid, nickname, password, email)
			values (?, ?, ?, ?)
			""";
		List<Pair<SQLType, Object>> params = List.of(of(JDBCType.VARCHAR, user.userId()),
			of(JDBCType.VARCHAR, user.nickname()), of(JDBCType.VARCHAR, user.password()),
			of(JDBCType.VARCHAR, user.email()));
		JdbcTemplate.update(insert, params);
	}

	@Override
	public User get(String id) {
		String findById = """
			select * from USERS where userid = ?
			""";
		User find = JdbcTemplate.executeOne(findById, User.class, List.of(of(JDBCType.VARCHAR, id)));
		if (find == null) {
			throw new BaseException(HttpStatus.NOT_FOUND, "user not found");
		}
		return find;
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
		return JdbcTemplate.execute(select, User.class, null);
	}
}
