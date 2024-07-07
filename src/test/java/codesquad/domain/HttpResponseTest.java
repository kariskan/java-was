package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

	@Test
	@DisplayName("상태 라인 설정")
	void setStatusLine() {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setStatusLine(HttpStatus.OK);

		assertThat(httpResponse.getStatusLine().status()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("본문 설정 및 조회")
	void setAndGetBody() {
		HttpResponse httpResponse = new HttpResponse();
		byte[] body = "Hello, world!".getBytes();
		httpResponse.setBody(body);

		assertThat(httpResponse.getBody()).isEqualTo(body);
	}

	@Test
	@DisplayName("헤더 설정 및 조회")
	void setAndGetHeader() {
		HttpHeader httpHeader = HttpHeader.of();
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setHeader(httpHeader);

		assertThat(httpResponse.getHeader()).isEqualTo(httpHeader);
	}

	@Test
	@DisplayName("응답 문자열 변환")
	void convertToString() {
		HttpHeader httpHeader = HttpHeader.of();
		byte[] body = "Hello, world!".getBytes();
		HttpResponse httpResponse = new HttpResponse(new StatusLine(HttpProtocol.HTTP11, HttpStatus.OK), httpHeader,
			new HttpBody(body));

		String expectedResponse = httpResponse.getStatusLine().toString() + httpHeader.toString() + new String(body);
		assertThat(httpResponse.toString()).isEqualTo(expectedResponse);
	}
}