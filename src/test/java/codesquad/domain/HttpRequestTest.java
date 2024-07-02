package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpRequestTest {

	@Test
	@DisplayName("HttpRequest 생성 및 RequestLine 반환 확인")
	void testGetRequestLine() throws Exception {
		RequestLine requestLine = new RequestLine(HttpMethod.GET, "/index.html", HttpProtocol.HTTP11);
		HttpHeader header = new HttpHeader(new HashMap<>());
		String body = "";

		HttpRequest httpRequest = new HttpRequest(requestLine, header, body);

		assertThat(httpRequest.requestLine()).isEqualTo(requestLine);
	}

	@Test
	@DisplayName("HttpRequest 생성 및 HttpHeader 반환 확인")
	void testGetHeader() throws Exception {
		RequestLine requestLine = new RequestLine(HttpMethod.GET, "/index.html", HttpProtocol.HTTP11);
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put("Host", "www.example.com");
		HttpHeader header = new HttpHeader(headersMap);
		String body = "";

		HttpRequest httpRequest = new HttpRequest(requestLine, header, body);

		assertThat(httpRequest.header()).isEqualTo(header);
	}

	@Test
	@DisplayName("HttpRequest 생성 및 Body 반환 확인")
	void testGetBody() throws Exception {
		RequestLine requestLine = new RequestLine(HttpMethod.POST, "/submit", HttpProtocol.HTTP11);
		HttpHeader header = new HttpHeader(new HashMap<>());
		String body = "name=John&age=30";

		HttpRequest httpRequest = new HttpRequest(requestLine, header, body);

		assertThat(httpRequest.body()).isEqualTo(body);
	}
}