package codesquad.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpRequestTest {

	private HttpRequest httpRequest;

	@BeforeEach
	void setUp() {
		RequestLine requestLine = new RequestLine(HttpMethod.GET, new Path("/index.html"), HttpProtocol.HTTP11);
		HttpHeader header = HttpHeader.of();
		String body = "";
		httpRequest = new HttpRequest(requestLine, header, body);
	}

	@Test
	@DisplayName("확장자를 반환한다")
	void getExtension() {
		assertThat(httpRequest.getExtension()).isEqualTo(".html");
	}

	@Test
	@DisplayName("GET 요청인지 확인한다")
	void isGet() {
		assertThat(httpRequest.isGet()).isTrue();
		assertThat(httpRequest.isPost()).isFalse();
	}

	@Test
	@DisplayName("POST 요청인지 확인한다")
	void isPost() {
		RequestLine postRequestLine = new RequestLine(HttpMethod.POST, new Path("/submit"), HttpProtocol.HTTP11);
		HttpRequest postRequest = new HttpRequest(postRequestLine, httpRequest.header(), "");
		assertThat(postRequest.isPost()).isTrue();
		assertThat(postRequest.isGet()).isFalse();
	}

	@Test
	@DisplayName("정적 요청인지 확인한다")
	void isStaticRequest() {
		RequestLine staticRequestLine = new RequestLine(HttpMethod.GET, new Path("/image.png"), HttpProtocol.HTTP11);
		HttpRequest staticRequest = new HttpRequest(staticRequestLine, httpRequest.header(), "");
		assertThat(staticRequest.isStaticRequest()).isTrue();
	}

	@Test
	@DisplayName("파라미터를 반환한다")
	void getParameters() {
		RequestLine parameterRequestLine = new RequestLine(HttpMethod.GET, new Path("/search?query=test"),
			HttpProtocol.HTTP11);
		HttpRequest parameterRequest = new HttpRequest(parameterRequestLine, httpRequest.header(), "");
		assertThat(parameterRequest.getParameters()).isNotNull();
		assertThat(parameterRequest.getParameters().getValueByKey("query")).isEqualTo("test");
	}
}