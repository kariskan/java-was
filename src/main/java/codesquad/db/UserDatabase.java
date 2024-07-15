package codesquad.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import codesquad.domain.HttpStatus;
import codesquad.domain.User;
import codesquad.error.BaseException;

public class UserDatabase implements Database<User> {

	private static final UserDatabase instance = new UserDatabase();
	private final Map<String, User> users = new ConcurrentHashMap<>();

	private UserDatabase() {
	}

	public static UserDatabase getInstance() {
		return instance;
	}

	@Override
	public void insert(String id, User user) {
		users.compute(id, (s, val) -> {
			if (val != null) {
				throw new BaseException(HttpStatus.BAD_REQUEST, "User already exists");
			}
			return user;
		});
	}

	@Override
	public User get(String id) {
		return Optional.ofNullable(users.get(id))
			.orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST, "User with id " + id + " does not exist"));
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

	public List<User> findAll() {
		return new ArrayList<>(users.values());
	}
}
