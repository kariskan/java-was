package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codesquad.data.UploadFile;
import codesquad.error.BaseException;

@DisplayName("HttpBody 클래스 테스트")
class HttpBodyTest {

	@Nested
	@DisplayName("bodyToMap 메서드")
	class BodyToMapTest {

		@Test
		@DisplayName("HTTP 바디를 맵으로 변환하는지 테스트")
		void bodyToMap_shouldConvertBodyToMap() {
			String bodyString = "name=John&age=30";
			HttpBody httpBody = new HttpBody(bodyString.getBytes());

			Map<String, String> result = httpBody.bodyToMap();

			assertThat(result).containsEntry("name", "John").containsEntry("age", "30");
		}
	}

	@Nested
	@DisplayName("bodyToMultipart 메서드")
	class BodyToMultipartTest {

		@Test
		@DisplayName("Multipart 바디를 맵으로 변환하는지 테스트")
		void bodyToMultipart_shouldConvertMultipartBodyToMap() throws UnsupportedEncodingException {
			String boundary = "boundary";
			String bodyString = "--boundary\r\n" +
								"Content-Disposition: form-data; name=\"field1\"\r\n\r\n" +
								"value1\r\n" +
								"--boundary\r\n" +
								"Content-Disposition: form-data; name=\"file\"; filename=\"file.txt\"\r\n" +
								"Content-Type: text/plain\r\n\r\n" +
								"file content\r\n" +
								"--boundary--";
			HttpBody httpBody = new HttpBody(bodyString.getBytes());

			Map<String, Object> result = httpBody.bodyToMultipart(boundary);

			assertThat(result).containsEntry("field1", "value1");
			UploadFile file = (UploadFile)result.get("file");
			assertThat(file.filename()).isEqualTo("file.txt");
			assertThat(new String(file.data())).isEqualTo("file content");
		}
	}
}
