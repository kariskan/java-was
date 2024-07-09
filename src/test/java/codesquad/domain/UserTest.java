package codesquad.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	@DisplayName("사용자를 생성하고 필드를 확인한다")
	void createUser() {
		User user = new User("user1", "nickname", "password", "email@example.com");
		assertThat(user.userId()).isEqualTo("user1");
		assertThat(user.nickname()).isEqualTo("nickname");
		assertThat(user.password()).isEqualTo("password");
		assertThat(user.email()).isEqualTo("email@example.com");
	}
}