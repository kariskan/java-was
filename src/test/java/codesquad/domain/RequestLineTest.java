package codesquad.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class RequestLineTest {

	@Test
	@DisplayName("RequestLine을 생성하고 URL을 반환한다")
	void createRequestLine() {
		Path path = new Path("/index.html");
		RequestLine requestLine = new RequestLine(HttpMethod.GET, path, HttpProtocol.HTTP11);
		assertThat(requestLine.getUrl()).isEqualTo("/index.html");
	}
}