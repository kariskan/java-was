package codesquad.handler.mapping;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import codesquad.domain.RequestLine;
import codesquad.handler.Handler;
import codesquad.handler.StaticResourceHandler;

public class HandlerMappingTest {

	@Test
	@DisplayName("HttpRequest에 대한 Handler 반환 확인")
	void testGetHandler() throws Exception {
		RequestLine requestLine = new RequestLine(HttpMethod.GET, "/index.html", HttpProtocol.HTTP11);
		HttpHeader header = new HttpHeader(new HashMap<>());
		String body = "";

		HttpRequest httpRequest = new HttpRequest(requestLine, header, body);
		Handler handler = HandlerMapping.getHandler(httpRequest);

		assertThat(handler).isInstanceOf(StaticResourceHandler.class);
	}
}