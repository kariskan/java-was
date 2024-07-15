package codesquad.utils;

import codesquad.data.User;

public class UserThreadLocal {

	private static final ThreadLocal<User> THREAD_LOCAL;

	static {
		THREAD_LOCAL = new ThreadLocal<>();
	}

	public static void set(User user) {
		THREAD_LOCAL.set(user);
	}

	public static User get() {
		return THREAD_LOCAL.get();
	}

	public static void remove() {
		THREAD_LOCAL.remove();
	}

	public static boolean isLogin() {
		return THREAD_LOCAL.get() != null;
	}
}
