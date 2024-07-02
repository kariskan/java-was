package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RequestLineTest {

	@Test
	@DisplayName("RequestLine 객체 생성 확인")
	void testCreateRequestLine() throws Exception {
		HttpMethod method = HttpMethod.GET;
		String url = "/index.html";
		HttpProtocol protocol = HttpProtocol.HTTP11;

		RequestLine requestLine = new RequestLine(method, url, protocol);

		assertThat(requestLine.method()).isEqualTo(method);
		assertThat(requestLine.url()).isEqualTo(url);
		assertThat(requestLine.protocol()).isEqualTo(protocol);
	}
}