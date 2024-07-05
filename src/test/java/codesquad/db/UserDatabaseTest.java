package codesquad.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import codesquad.domain.User;
import org.junit.jupiter.api.AfterAll;
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
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> userDatabase.insert("john", user));
		assertThat("User with id john already exists").isEqualTo(exception.getMessage());
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
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> userDatabase.get("unknown"));
		assertThat("User with id unknown does not exist").isEqualTo(exception.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 사용자를 업데이트하려 하면 예외를 발생시킨다")
	void updateUserNotFound() {
		User updatedUser = new User("unknown", "Unknown User", "password", "email");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> userDatabase.update("unknown", updatedUser));
		assertThat("User with id unknown does not exist").isEqualTo(exception.getMessage());
	}

	@Test
	@DisplayName("사용자를 삭제한다")
	void deleteUser() {
		User user = new User("john", "John Doe", "password", "email");
		userDatabase.insert("john", user);
		userDatabase.delete("john");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> userDatabase.get("john"));
		assertThat("User with id john does not exist").isEqualTo(exception.getMessage());
	}
}