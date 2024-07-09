package codesquad.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpRequestUtilTest {

	@Test
	@DisplayName("HttpRequest 파싱 테스트")
	void testParseRequest() throws Exception {
		String rawRequest = "GET /register.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
		ByteArrayInputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());

		HttpRequest httpRequest = HttpRequestUtil.parseRequest(inputStream);

		assertThat(httpRequest.requestLine().method()).isEqualTo(HttpMethod.GET);
		assertThat(httpRequest.requestLine().getUrl()).isEqualTo("/register.html");
		assertThat(httpRequest.requestLine().protocol()).isEqualTo(HttpProtocol.HTTP11);
		assertThat(httpRequest.header().getHeaderValue("Host")).isEqualTo("localhost");
	}

	@Test
	@DisplayName("Invalid HttpRequest 파싱 테스트")
	void testParseInvalidRequest() throws IOException {
		String rawRequest = "GET /register.html HTTP/1.1\r\nInvalidHeader\r\n\r\n";
		ByteArrayInputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());

		assertThatThrownBy(() -> HttpRequestUtil.parseRequest(inputStream));
	}
}