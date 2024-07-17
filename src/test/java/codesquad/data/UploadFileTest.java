package codesquad.data;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UploadFile 클래스 테스트")
class UploadFileTest {

	@Nested
	@DisplayName("getExtension 메서드")
	class GetExtensionTest {

		@Test
		@DisplayName("확장자를 정확히 추출하는지 테스트")
		void getExtension_shouldReturnCorrectExtension() {
			UploadFile file = new UploadFile(1L, "test.png", new byte[0]);

			String extension = file.getExtension();

			assertThat(extension).isEqualTo(".png");
		}

		@Test
		@DisplayName("대문자 확장자를 소문자로 변환하는지 테스트")
		void getExtension_shouldConvertToLowerCase() {
			UploadFile file = new UploadFile(1L, "test.PNG", new byte[0]);

			String extension = file.getExtension();

			assertThat(extension).isEqualTo(".png");
		}
	}
}
