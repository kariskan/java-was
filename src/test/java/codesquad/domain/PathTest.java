package codesquad.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {

	@Test
	@DisplayName("URL과 파라미터를 분리하여 생성한다")
	void createPath() {
		Path path = new Path("/search?query=test");
		assertThat(path.getUrl()).isEqualTo("/search");
		assertThat(path.getParameters().getValueByKey("query")).isEqualTo("test");
	}

	@Test
	@DisplayName("파라미터가 없는 URL을 생성한다")
	void createPathWithoutParameters() {
		Path path = new Path("/index.html");
		assertThat(path.getUrl()).isEqualTo("/index.html");
		assertThat(path.getParameters()).isNull();
	}
}