package codesquad.domain;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.utils.StringUtils;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpHeaderTest {

	private HttpHeader httpHeader;

	@BeforeEach
	void setUp() {
		httpHeader = new HttpHeader(new HashMap<>());
	}

	@Test
	@DisplayName("기본 헤더가 설정된다")
	void setDefaultHeaders() {
		httpHeader.setDefaultHeaders();
		assertThat(httpHeader.existsHeaderValue("Date")).isTrue();
	}

	@Test
	@DisplayName("헤더 값을 설정한다")
	void setHeaderValue() {
		httpHeader.setHeaderValue("Content-Type", "application/json");
		assertThat(httpHeader.getHeaderValue("Content-Type")).isEqualTo("application/json");
	}

	@Test
	@DisplayName("헤더 값을 조회한다")
	void getHeaderValue() {
		httpHeader.setHeaderValue("Content-Type", "application/json");
		assertThat(httpHeader.getHeaderValue("Content-Type")).isEqualTo("application/json");
	}

	@Test
	@DisplayName("헤더 값이 존재하는지 확인한다")
	void existsHeaderValue() {
		httpHeader.setHeaderValue("Content-Type", "application/json");
		assertThat(httpHeader.existsHeaderValue("Content-Type")).isTrue();
		assertThat(httpHeader.existsHeaderValue("Authorization")).isFalse();
	}

	@Test
	@DisplayName("헤더를 문자열로 변환한다")
	void testToString() {
		httpHeader.setHeaderValue("Content-Type", "application/json");
		String expected = "Content-Type: application/json" + StringUtils.lineSeparator() + StringUtils.lineSeparator();
		assertThat(httpHeader.toString()).isEqualTo(expected);
	}
}