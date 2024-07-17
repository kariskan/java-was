package codesquad.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ContentType 클래스 테스트")
class ContentTypeTest {

	@Nested
	@DisplayName("from 메서드")
	class FromTest {

		@ParameterizedTest(name = "{0} 확장자에 대해 ContentType 반환 테스트")
		@ValueSource(strings = {".html", ".css", ".js", ".png", ".jpeg"})
		void from_shouldReturnCorrectContentType(String extension) {
			ContentType contentType = ContentType.from(extension);

			assertThat(contentType.getExtension()).isEqualTo(extension);
		}

		@Test
		@DisplayName("존재하지 않는 확장자에 대해 기본 ContentType 반환 테스트")
		void from_shouldReturnDefaultContentTypeForUnknownExtension() {
			ContentType contentType = ContentType.from(".unknown");

			assertThat(contentType).isEqualTo(ContentType.HTML);
		}
	}

	@Nested
	@DisplayName("getMimeType 메서드")
	class GetMimeTypeTest {

		@Test
		@DisplayName("올바른 MIME 타입을 반환하는지 테스트")
		void getMimeType_shouldReturnCorrectMimeType() {
			ContentType contentType = ContentType.PNG;

			String mimeType = contentType.getMimeType();

			assertThat(mimeType).isEqualTo("image/png");
		}
	}
}
