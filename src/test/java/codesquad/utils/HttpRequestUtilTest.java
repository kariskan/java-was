package codesquad.utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;

public class HttpRequestUtilTest {

	@Test
	@DisplayName("HttpRequest 파싱 테스트")
	void testParseRequest() throws Exception {
		String rawRequest = "GET /index.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
		ByteArrayInputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());

		HttpRequest httpRequest = HttpRequestUtil.parseRequest(inputStream);

		assertThat(httpRequest.requestLine().method()).isEqualTo(HttpMethod.GET);
		assertThat(httpRequest.requestLine().url()).isEqualTo("/index.html");
		assertThat(httpRequest.requestLine().protocol()).isEqualTo(HttpProtocol.HTTP11);
		assertThat(httpRequest.header().getHeaderValue("Host")).isEqualTo("localhost");
		assertThat(httpRequest.body()).isEmpty();
	}

	@Test
	@DisplayName("Invalid HttpRequest 파싱 테스트")
	void testParseInvalidRequest() {
		String rawRequest = "GET /index.html HTTP/1.1\r\nInvalidHeader\r\n\r\n";
		ByteArrayInputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());

		assertThrows(IllegalArgumentException.class, () -> {
			HttpRequestUtil.parseRequest(inputStream);
		});
	}
}