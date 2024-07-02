package codesquad.domain;

import static codesquad.utils.StringUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StatusLineTest {

	@Test
	@DisplayName("StatusLine ok() 메서드 확인")
	void testOk() throws Exception {
		StatusLine statusLine = StatusLine.ok();

		assertThat(statusLine.protocol()).isEqualTo(HttpProtocol.HTTP11);
		assertThat(statusLine.status()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("StatusLine 객체를 문자열로 변환 확인")
	void testToString() throws Exception {
		StatusLine statusLine = new StatusLine(HttpProtocol.HTTP11, HttpStatus.OK);
		String expected = "HTTP/1.1 200 OK" + lineSeparator();

		assertThat(statusLine.toString()).isEqualTo(expected);
	}
}