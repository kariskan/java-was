package codesquad.db;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;

public class SessionDatabase implements Database<String, String> {

	private static final SessionDatabase instance = new SessionDatabase();
	private final Map<String, String> sessions = new ConcurrentHashMap<>();

	private SessionDatabase() {
	}

	public static SessionDatabase getInstance() {
		return instance;
	}

	@Override
	public Long insert(String sessionId, String id) {
		if (sessions.containsValue(sessionId)) {
			sessions.remove(sessionId);
		}
		sessions.put(sessionId, id);
		return null;
	}

	@Override
	public String get(String sessionId) {
		return sessions.get(sessionId);
	}

	@Override
	public void update(String sessionId, String id) {
		sessions.compute(sessionId, (key, value) -> {
			if (value == null) {
				throw new BaseException(HttpStatus.BAD_REQUEST, "User not exist");
			}
			return id;
		});
	}

	@Override
	public void delete(String sessionId) {
		sessions.remove(sessionId);
	}

	@Override
	public List<String> findAll() {
		return null;
	}
}
