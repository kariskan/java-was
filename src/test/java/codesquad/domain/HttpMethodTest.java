package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import codesquad.error.BaseException;

@DisplayName("HttpMethod 클래스 테스트")
class HttpMethodTest {

	@Nested
	@DisplayName("from 메서드")
	class FromTest {

		@ParameterizedTest(name = "{0} 메서드에 대해 HttpMethod 반환 테스트")
		@ValueSource(strings = {"GET", "POST", "PUT", "DELETE"})
		void from_shouldReturnCorrectHttpMethod(String method) {
			HttpMethod httpMethod = HttpMethod.from(method);

			assertThat(httpMethod.name()).isEqualTo(method);
		}

		@Test
		@DisplayName("알 수 없는 메서드에 대해 예외를 던지는지 테스트")
		void from_shouldThrowExceptionForUnknownMethod() {
			assertThatThrownBy(() -> HttpMethod.from("UNKNOWN"))
				.isInstanceOf(BaseException.class)
				.hasMessageContaining("unknown method");
		}
	}
}
