package codesquad.db;

import codesquad.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDatabase implements Database<User> {

	private static final UserDatabase instance = new UserDatabase();
	private final Map<String, User> users = new HashMap<>();

	private UserDatabase() {
	}

	public static UserDatabase getInstance() {
		return instance;
	}

	@Override
	public void insert(String id, User user) {
		if (users.containsKey(id)) {
			throw new IllegalArgumentException("User with id " + id + " already exists");
		}
		users.put(id, user);
	}

	@Override
	public User get(String id) {
		return Optional.ofNullable(users.get(id))
			.orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist"));
	}

	@Override
	public void update(String id, User user) {
		if (users.containsKey(id)) {
			users.put(id, user);
		}
		throw new IllegalArgumentException("User with id " + id + " does not exist");
	}

	@Override
	public void delete(String id) {
		users.remove(id);
	}

	public void clear() {
		users.clear();
	}
}
