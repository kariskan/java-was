package codesquad.domain;

import codesquad.utils.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class StatusLineTest {

	@Test
	@DisplayName("OK 상태 라인을 생성한다")
	void createStatusLine() {
		StatusLine statusLine = StatusLine.ok();
		assertThat(statusLine.protocol()).isEqualTo(HttpProtocol.HTTP11);
		assertThat(statusLine.status()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("상태 라인을 문자열로 변환한다")
	void testToString() {
		StatusLine statusLine = StatusLine.ok();
		String expected = "HTTP/1.1 200 OK" + StringUtils.lineSeparator();
		assertThat(statusLine.toString()).isEqualTo(expected);
	}
}