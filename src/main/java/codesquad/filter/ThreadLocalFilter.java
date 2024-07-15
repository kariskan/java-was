package codesquad.filter;

import codesquad.db.SessionDatabase;
import codesquad.db.UserDatabase;
import codesquad.domain.Cookie;
import codesquad.domain.HttpRequest;
import codesquad.data.User;
import codesquad.utils.UserThreadLocal;

public class ThreadLocalFilter {

	public static void doFilter(HttpRequest request) {
		User user = getUserByCookie(request);
		if (user == null) {
			return;
		}
		UserThreadLocal.set(user);
	}

	private static User getUserByCookie(HttpRequest request) {
		Cookie cookie = request.getCookie("SID");
		if (cookie != null) {
			String userId = SessionDatabase.getInstance().get(cookie.getValue());
			if (userId == null) {
				return null;
			}
			return UserDatabase.getInstance().get(userId);
		}
		return null;
	}
}
