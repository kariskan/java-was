package codesquad.utils;

import codesquad.data.UploadFile;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TemplateEngineTest {

	@Mock
	UploadFile uploadFile;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRenderVariables() {
		Map<String, Object> context = new HashMap<>();
		context.put("name", "John Doe");

		String template = "Hello, {{ name }}!";
		String result = TemplateEngine.render(template, context);

		assertThat(result).isEqualTo("Hello, John Doe!");
	}

	@Test
	void testRenderObjectVariables() throws IOException {
		when(uploadFile.getExtension()).thenReturn(".png");
		when(uploadFile.data()).thenReturn(new byte[]{1, 2, 3, 4});

		Map<String, Object> context = new HashMap<>();
		context.put("file", uploadFile);

		String template = "<img src=\"{{ file }}\" />";
		String result = TemplateEngine.render(template, context);

		String base64Data = Base64.getEncoder().encodeToString(uploadFile.data());
		assertThat(result).isEqualTo("<img src=\"data:image/png;base64," + base64Data + "\" />");
	}

	@Test
	void testRenderIfElseStatements() {
		Map<String, Object> context = new HashMap<>();
		context.put("condition", true);

		String template = "{% if condition %}Condition is true{% else %}Condition is false{% endif %}";
		String result = TemplateEngine.render(template, context);

		assertThat(result).isEqualTo("Condition is true");

		context.put("condition", false);
		result = TemplateEngine.render(template, context);

		assertThat(result).isEqualTo("Condition is false");
	}
}
