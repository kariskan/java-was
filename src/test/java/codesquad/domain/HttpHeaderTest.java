package codesquad.domain;

import static codesquad.utils.StringUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpHeaderTest {

	@Test
	@DisplayName("headers에 headerName이 존재하는지 확인")
	void testExistsHeaderValue() throws Exception {
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		HttpHeader httpHeader = new HttpHeader(headersMap);

		assertThat(httpHeader.existsHeaderValue("Content-Type")).isTrue();
		assertThat(httpHeader.existsHeaderValue("Accept")).isFalse();
	}

	@Test
	@DisplayName("headers에서 headerName에 해당하는 값을 반환")
	void testGetHeaderValue() throws Exception {
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		HttpHeader httpHeader = new HttpHeader(headersMap);

		assertThat(httpHeader.getHeaderValue("Content-Type")).isEqualTo("application/json");
		assertThat(httpHeader.getHeaderValue("Accept")).isNull();
	}

	@Test
	@DisplayName("headers를 올바르게 문자열로 변환")
	void testToString() throws Exception {
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		headersMap.put("Accept", "application/xml");
		HttpHeader httpHeader = new HttpHeader(headersMap);

		String expected = "Accept: application/xml" + lineSeparator()
						  + "Content-Type: application/json" + lineSeparator();
		assertThat(httpHeader.toString()).isEqualTo(expected);
	}
}
