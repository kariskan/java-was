// package codesquad.utils;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// import codesquad.domain.HttpHeader;
// import codesquad.domain.HttpProtocol;
// import codesquad.domain.HttpResponse;
// import codesquad.domain.HttpStatus;
// import codesquad.domain.StatusLine;
// import java.io.ByteArrayOutputStream;
// import java.util.Map;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
//
// public class HttpResponseUtilTest {
//
// 	@Test
// 	@DisplayName("HttpResponse 쓰기 테스트")
// 	void testWriteResponse() throws Exception {
// 		StatusLine statusLine = new StatusLine(HttpProtocol.HTTP11, HttpStatus.OK);
// 		HttpHeader header = new HttpHeader(Map.of("Content-Type", "text/html"));
// 		byte[] body = "<html><body>Hello World</body></html>".getBytes();
// 		HttpResponse httpResponse = new HttpResponse();
// 		httpResponse.setStatusLine();
// 		httpResponse.setHeader(header);
// 		httpResponse.setBody(body);
//
// 		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
// 		HttpResponseUtil.writeResponse(outputStream, httpResponse);
//
// 		String expectedResponse = statusLine.toString() + header + new String(body);
// 		assertThat(outputStream.toString()).isEqualTo(expectedResponse);
// 	}
// }