package codesquad.db;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codesquad.error.BaseException;

@DisplayName("SessionDatabase 클래스 테스트")
class SessionDatabaseTest {

	SessionDatabase sessionDatabase;

	@BeforeEach
	void setUp() {
		sessionDatabase = SessionDatabase.getInstance();
	}

	@Nested
	@DisplayName("insert 메서드")
	class InsertTest {

		@Test
		@DisplayName("세션을 정상적으로 삽입하는지 테스트")
		void insert_shouldInsertSession() {
			sessionDatabase.insert("session1", "user1");

			String userId = sessionDatabase.get("session1");

			assertThat(userId).isEqualTo("user1");
		}
	}

	@Nested
	@DisplayName("get 메서드")
	class GetTest {

		@Test
		@DisplayName("세션 ID로 유저 ID를 가져오는지 테스트")
		void get_shouldReturnUserIdBySessionId() {
			sessionDatabase.insert("session1", "user1");

			String userId = sessionDatabase.get("session1");

			assertThat(userId).isEqualTo("user1");
		}
	}

	@Nested
	@DisplayName("update 메서드")
	class UpdateTest {

		@Test
		@DisplayName("세션을 업데이트하는지 테스트")
		void update_shouldUpdateSession() {
			sessionDatabase.insert("session1", "user1");
			sessionDatabase.update("session1", "user2");

			String userId = sessionDatabase.get("session1");

			assertThat(userId).isEqualTo("user2");
		}

		@Test
		@DisplayName("존재하지 않는 세션을 업데이트 시 예외를 던지는지 테스트")
		void update_shouldThrowExceptionWhenSessionNotExist() {
			assertThatThrownBy(() -> sessionDatabase.update("session1", "user2"))
				.isInstanceOf(BaseException.class)
				.hasMessageContaining("User not exist");
		}
	}

	@Nested
	@DisplayName("delete 메서드")
	class DeleteTest {

		@Test
		@DisplayName("세션을 삭제하는지 테스트")
		void delete_shouldDeleteSession() {
			sessionDatabase.insert("session1", "user1");
			sessionDatabase.delete("session1");

			String userId = sessionDatabase.get("session1");

			assertThat(userId).isNull();
		}
	}
}
