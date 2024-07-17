package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Cookie 클래스 테스트")
class CookieTest {

	@Nested
	@DisplayName("toString 메서드")
	class ToStringTest {

		@Test
		@DisplayName("쿠키 정보를 문자열로 변환하는지 테스트")
		void toString_shouldReturnCookieString() {
			Cookie cookie = new Cookie("sessionId", "abc123", Duration.ofDays(1), "/");

			String result = cookie.toString();

			assertThat(result).isEqualTo("sessionId=abc123; Max-Age=86400000; Path=/");
		}

		@Test
		@DisplayName("maxAge가 없는 경우 처리 테스트")
		void toString_shouldHandleNullMaxAge() {
			Cookie cookie = new Cookie("sessionId", "abc123", null, "/");

			String result = cookie.toString();

			assertThat(result).isEqualTo("sessionId=abc123; Path=/");
		}

		@Test
		@DisplayName("path가 없는 경우 처리 테스트")
		void toString_shouldHandleNullPath() {
			Cookie cookie = new Cookie("sessionId", "abc123", Duration.ofDays(1), null);

			String result = cookie.toString();

			assertThat(result).isEqualTo("sessionId=abc123; Max-Age=86400000");
		}
	}
}
