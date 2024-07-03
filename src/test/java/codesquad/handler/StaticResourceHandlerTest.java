package codesquad.handler;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.RequestLine;
import codesquad.domain.StatusLine;

public class StaticResourceHandlerTest {

	@Test
	@DisplayName("정적 자원에 대한 HttpResponse 반환 확인")
	void testDoService() throws Exception {
		RequestLine requestLine = new RequestLine(HttpMethod.GET, "/index.html", HttpProtocol.HTTP11);
		HttpHeader header = new HttpHeader(new HashMap<>());
		String body = "";

		HttpRequest httpRequest = new HttpRequest(requestLine, header, body);
		StaticResourceHandler handler = new StaticResourceHandler(httpRequest);

		HttpResponse httpResponse = handler.doService();

		assertThat(httpResponse.statusLine()).isEqualTo(StatusLine.ok());
		assertThat(httpResponse.body()).isNotEmpty();
	}
}