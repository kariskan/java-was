package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codesquad.error.BaseException;

@DisplayName("HttpProtocol 클래스 테스트")
class HttpProtocolTest {

	@Nested
	@DisplayName("from 메서드")
	class FromTest {

		@Test
		@DisplayName("프로토콜 문자열에 대해 HttpProtocol 반환 테스트")
		void from_shouldReturnCorrectHttpProtocol() {
			HttpProtocol httpProtocol = HttpProtocol.from("HTTP/1.1");

			assertThat(httpProtocol).isEqualTo(HttpProtocol.HTTP11);
		}

		@Test
		@DisplayName("알 수 없는 프로토콜에 대해 예외를 던지는지 테스트")
		void from_shouldThrowExceptionForUnknownProtocol() {
			assertThatThrownBy(() -> HttpProtocol.from("HTTP/2.0"))
				.isInstanceOf(BaseException.class)
				.hasMessageContaining("Unknown protocol");
		}
	}
}
