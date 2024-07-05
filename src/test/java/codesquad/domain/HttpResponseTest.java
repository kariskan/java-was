package codesquad.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {

	private HttpResponse httpResponse;

	@BeforeEach
	void setUp() {
		httpResponse = new HttpResponse();
	}

	@Test
	@DisplayName("상태 라인을 설정한다")
	void setStatusLine() {
		httpResponse.setStatusLine(HttpStatus.OK);
		assertThat(httpResponse.getStatusLine().status()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("헤더를 설정한다")
	void setHeader() {
		HttpHeader header = HttpHeader.of();
		httpResponse.setHeader(header);
		assertThat(httpResponse.getHeader()).isEqualTo(header);
	}

	@Test
	@DisplayName("본문을 설정한다")
	void setBody() {
		byte[] body = "Hello, World!".getBytes();
		httpResponse.setBody(body);
		assertThat(httpResponse.getBody()).isEqualTo(body);
	}

	@Test
	@DisplayName("HTTP 응답을 문자열로 변환한다")
	void testToString() {
		httpResponse.setStatusLine(HttpStatus.OK);
		HttpHeader header = HttpHeader.of();
		httpResponse.setHeader(header);
		String body = "Hello, World!";
		httpResponse.setBody(body.getBytes());
		String expected = httpResponse.getStatusLine().toString() + header.toString() + body;
		assertThat(httpResponse.toString()).isEqualTo(expected);
	}
}