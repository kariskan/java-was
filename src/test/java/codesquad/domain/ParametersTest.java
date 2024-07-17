package codesquad.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import codesquad.error.BaseException;

public class ParametersTest {

	@Test
	@DisplayName("파라미터에서 키에 해당하는 값을 정상적으로 반환하는지 테스트")
	public void testGetValueByKey() {
		Parameters parameters = new Parameters("userId=john&password=1234&email=john@example.com");
		Assertions.assertThat(parameters.getValueByKey("userId")).isEqualTo("john");
		Assertions.assertThat(parameters.getValueByKey("password")).isEqualTo("1234");
		Assertions.assertThat(parameters.getValueByKey("email")).isEqualTo("john@example.com");
	}

	@Test
	@DisplayName("키가 존재하지 않을 때 예외를 던지는지 테스트")
	public void testGetValueByKeyThrowsException() {
		Parameters parameters = new Parameters("userId=john&password=1234&email=john@example.com");
		Assertions.assertThatThrownBy(() -> parameters.getValueByKey("nickname"))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("Key nickname not found");
	}

	@ParameterizedTest
	@CsvSource({
		"userId=john&password=1234&email=john@example.com, userId, john",
		"userId=jane&password=abcd&email=jane@example.com, email, jane@example.com",
		"userId=doe&password=5678&email=doe@example.com, password, 5678"
	})
	@DisplayName("다양한 파라미터 입력에 대한 getValueByKey 테스트")
	public void testGetValueByKeyParameterized(String input, String key, String expectedValue) {
		Parameters parameters = new Parameters(input);
		Assertions.assertThat(parameters.getValueByKey(key)).isEqualTo(expectedValue);
	}
}