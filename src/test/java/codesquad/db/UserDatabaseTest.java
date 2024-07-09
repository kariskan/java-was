package codesquad.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.domain.User;
import codesquad.error.BaseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDatabaseTest {

	private UserDatabase userDatabase;

	@BeforeEach
	void setUp() {
		userDatabase = UserDatabase.getInstance();
	}

	@AfterEach
	void tearDown() {
		userDatabase.clear();
	}

	@Test
	@DisplayName("사용자를 추가한다")
	void insertUser() {
		User user = new User("john", "John Doe", "password", "email");
		userDatabase.insert("john", user);
		User fetchedUser = userDatabase.get("john");
		assertThat(user).isEqualTo(fetchedUser);
	}

	@Test
	@DisplayName("이미 존재하는 사용자를 추가하려 하면 예외를 발생시킨다")
	void insertUserAlreadyExists() {
		User user = new User("john", "John Doe", "password", "email");
		userDatabase.insert("john", user);
		assertThatThrownBy(() -> userDatabase.insert("john", user));
	}

	@Test
	@DisplayName("존재하는 사용자를 조회한다")
	void getUser() {
		User user = new User("john", "John Doe", "password", "email");
		userDatabase.insert("jane", user);
		User fetchedUser = userDatabase.get("jane");
		assertThat(user).isEqualTo(fetchedUser);
	}

	@Test
	@DisplayName("존재하지 않는 사용자를 조회하려 하면 예외를 발생시킨다")
	void getUserNotFound() {
		Assertions.assertThatThrownBy(() -> userDatabase.get("unknown"))
			.isInstanceOf(BaseException.class);
	}

	@Test
	@DisplayName("존재하지 않는 사용자를 업데이트하려 하면 예외를 발생시킨다")
	void updateUserNotFound() {
		User updatedUser = new User("unknown", "Unknown User", "password", "email");
		assertThatThrownBy(() -> userDatabase.update("unknown", updatedUser));
	}

	@Test
	@DisplayName("사용자를 삭제한다")
	void deleteUser() {
		User user = new User("john", "John Doe", "password", "email");
		userDatabase.insert("john", user);
		userDatabase.delete("john");
		assertThatThrownBy(() -> userDatabase.get("john"));
	}
}