package codesquad.utils;

import codesquad.data.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserThreadLocalTest {

	@Test
	void testSetAndGet() {
		User user = new User("user1", "nickname", "password", "email");
		UserThreadLocal.set(user);

		User retrievedUser = UserThreadLocal.get();
		assertThat(retrievedUser).isEqualTo(user);
	}

	@Test
	void testRemove() {
		User user = new User("user1", "nickname", "password", "email");
		UserThreadLocal.set(user);
		UserThreadLocal.remove();

		User retrievedUser = UserThreadLocal.get();
		assertThat(retrievedUser).isNull();
	}

	@Test
	void testIsLogin() {
		UserThreadLocal.remove();
		assertThat(UserThreadLocal.isLogin()).isFalse();

		User user = new User("user1", "nickname", "password", "email");
		UserThreadLocal.set(user);

		assertThat(UserThreadLocal.isLogin()).isTrue();
	}
}
