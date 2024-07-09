package codesquad.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codesquad.domain.HttpBody;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import codesquad.domain.Path;
import codesquad.domain.RequestLine;

public class HandlerMappingTest {

	@Test
	@DisplayName("정적 파일 요청에 대해 StaticRequestHandler를 반환하는지 테스트")
	public void testGetStaticHandler() {
		HttpRequest request = new HttpRequest(
			new RequestLine(HttpMethod.GET,
				new Path("/index.html"), HttpProtocol.HTTP11),
			new HttpHeader(null),
			new HttpBody(null));
		Handler handler = HandlerMapping.getHandler(request);
		Assertions.assertThat(handler).isInstanceOf(StaticRequestHandler.class);
	}

	@Test
	@DisplayName("동적 요청에 대해 SignUpHandler를 반환하는지 테스트")
	public void testGetDynamicHandler() {
		HttpRequest request = new HttpRequest(
			new RequestLine(HttpMethod.POST,
				new Path("/create"), HttpProtocol.HTTP11),
			new HttpHeader(null),
			new HttpBody(null));
		Handler handler = HandlerMapping.getHandler(request);
		Assertions.assertThat(handler).isInstanceOf(SignUpHandler.class);
	}

	@Test
	@DisplayName("유효하지 않은 요청에 대해 예외를 던지는지 테스트")
	public void testGetInvalidHandler() {
		HttpRequest request = new HttpRequest(
			new RequestLine(HttpMethod.GET,
				new Path("/invalid"), HttpProtocol.HTTP11),
			new HttpHeader(null),
			new HttpBody(null));
		Assertions.assertThatThrownBy(() -> HandlerMapping.getHandler(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Invalid request");
	}
}