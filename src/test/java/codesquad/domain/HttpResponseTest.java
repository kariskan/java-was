package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {

	@Test
	@DisplayName("HttpResponse 객체를 문자열로 변환 확인")
	void testToString() throws Exception {
		StatusLine statusLine = new StatusLine(HttpProtocol.HTTP11, HttpStatus.OK);
		HttpHeader header = new HttpHeader(Map.of("Content-Type", "text/html"));
		String body = "<html><body>Hello World</body></html>";

		HttpResponse httpResponse = new HttpResponse(statusLine, header, body);
		String expected = statusLine.toString() + header.toString() + body;

		assertThat(httpResponse.toString()).isEqualTo(expected);
	}

	@Test
	@DisplayName("HttpResponse 객체를 바이트 배열로 변환 확인")
	void testGetBytes() throws Exception {
		StatusLine statusLine = new StatusLine(HttpProtocol.HTTP11, HttpStatus.OK);
		HttpHeader header = new HttpHeader(Map.of("Content-Type", "text/html"));
		String body = "<html><body>Hello World</body></html>";

		HttpResponse httpResponse = new HttpResponse(statusLine, header, body);
		byte[] expected = httpResponse.toString().getBytes();

		assertThat(httpResponse.getBytes()).isEqualTo(expected);
	}
}