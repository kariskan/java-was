package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("HttpStatus 클래스 테스트")
class HttpStatusTest {

	@Nested
	@DisplayName("from 메서드")
	class FromTest {

		@ParameterizedTest(name = "{0} 코드에 대해 HttpStatus 반환 테스트")
		@ValueSource(strings = {"200", "404", "500"})
		void from_shouldReturnCorrectHttpStatus(String code) {
			HttpStatus httpStatus = HttpStatus.from(code);

			assertThat(httpStatus.getCode()).isEqualTo(Integer.parseInt(code));
		}

		@Test
		@DisplayName("알 수 없는 코드에 대해 null 반환 테스트")
		void from_shouldReturnNullForUnknownCode() {
			HttpStatus httpStatus = HttpStatus.from("999");

			assertThat(httpStatus).isNull();
		}
	}

	@Nested
	@DisplayName("toString 메서드")
	class ToStringTest {

		@Test
		@DisplayName("HttpStatus를 문자열로 변환하는지 테스트")
		void toString_shouldReturnStatusString() {
			HttpStatus httpStatus = HttpStatus.OK;

			String result = httpStatus.toString();

			assertThat(result).isEqualTo("200 OK");
		}
	}
}
