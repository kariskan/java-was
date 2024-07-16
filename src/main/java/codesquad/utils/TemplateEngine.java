package codesquad.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateEngine {

	private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\s*\\}\\}");
	private static final Pattern OBJECT_VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\.(\\w+)\\s*\\}\\}");
	private static final Pattern IF_ELSE_PATTERN = Pattern.compile(
		"\\{%\\s*if\\s+(\\w+)\\s*%\\}(.*?)\\{%\\s*else\\s*%\\}(.*?)\\{%\\s*endif\\s*%\\}", Pattern.DOTALL);
	private static final Pattern IF_PATTERN = Pattern.compile("\\{%\\s*if\\s+(\\w+)\\s*%\\}(.*?)\\{%\\s*endif\\s*%\\}",
		Pattern.DOTALL);
	private static final Pattern FOR_EACH_PATTERN = Pattern.compile(
		"\\{%\\s*for\\s+(\\w+)\\s+in\\s+(\\w+)\\s*%\\}(.*?)\\{%\\s*endfor\\s*%\\}", Pattern.DOTALL);
	private static final Pattern INCLUDE_PATTERN = Pattern.compile("\\{%\\s*include\\s+\"([^\"]+)\"\\s*%\\}");
	private static final Logger log = LoggerFactory.getLogger(TemplateEngine.class);

	private TemplateEngine() {
	}

	public static String render(String template, Map<String, Object> context) {
		try {
			template = renderVariables(template, context);
			template = renderIfElseStatements(template, context);
			template = renderIfStatements(template, context);
			template = renderForEachStatements(template, context);
			template = renderIncludes(template, context);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	private static String renderIncludes(String template, Map<String, Object> context) throws IOException {
		Matcher matcher = INCLUDE_PATTERN.matcher(template);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String includeFile = matcher.group(1);
			String includedContent = readFile(includeFile);
			includedContent = render(includedContent, context); // 포함된 내용도 템플릿 엔진을 통해 처리
			matcher.appendReplacement(sb, Matcher.quoteReplacement(includedContent));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static String readFile(String filePath) throws IOException {
		URL resource = TemplateEngine.class.getClassLoader().getResource(filePath);
		if (resource == null) {
			throw new IOException("Resource not found: " + filePath);
		}
		try (InputStream inputStream = resource.openStream()) {
			return new String(inputStream.readAllBytes());
		}
	}

	private static String renderVariables(String template, Map<String, Object> context) {
		Matcher matcher = VARIABLE_PATTERN.matcher(template);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String variableName = matcher.group(1);
			Object value = context.get(variableName);
			matcher.appendReplacement(sb, value != null ? Matcher.quoteReplacement(value.toString()) : "");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static String renderObjectVariables(String template, Map<String, Object> context) {
		Matcher matcher = OBJECT_VARIABLE_PATTERN.matcher(template);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String objectName = matcher.group(1);
			String propertyName = matcher.group(2);
			Object object = context.get(objectName);
			if (object != null) {
				try {
					Field field = object.getClass().getDeclaredField(propertyName);
					field.setAccessible(true);
					Object value = field.get(object);
					matcher.appendReplacement(sb, value != null ? Matcher.quoteReplacement(value.toString()) : "");
				} catch (NoSuchFieldException | IllegalAccessException e) {
					matcher.appendReplacement(sb, "");
				}
			} else {
				matcher.appendReplacement(sb, "");
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static String renderIfElseStatements(String template, Map<String, Object> context) throws IOException {
		Matcher matcher = IF_ELSE_PATTERN.matcher(template);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String condition = matcher.group(1);
			String ifContent = matcher.group(2);
			String elseContent = matcher.group(3);
			boolean conditionValue = Boolean.parseBoolean(context.getOrDefault(condition, false).toString());
			if (conditionValue) {
				ifContent = render(ifContent, context); // if 조건 내부의 템플릿 처리
				matcher.appendReplacement(sb, ifContent);
			} else {
				elseContent = render(elseContent, context); // else 조건 내부의 템플릿 처리
				matcher.appendReplacement(sb, elseContent);
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static String renderIfStatements(String template, Map<String, Object> context) throws IOException {
		Matcher matcher = IF_PATTERN.matcher(template);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String condition = matcher.group(1);
			String content = matcher.group(2);
			boolean conditionValue = Boolean.parseBoolean(context.getOrDefault(condition, false).toString());
			if (conditionValue) {
				content = render(content, context); // if 조건 내부의 템플릿 처리
				matcher.appendReplacement(sb, content);
			} else {
				matcher.appendReplacement(sb, "");
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static String renderForEachStatements(String template, Map<String, Object> context) throws IOException {
		Matcher matcher = FOR_EACH_PATTERN.matcher(template);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String variableName = matcher.group(1);
			String listName = matcher.group(2);
			String content = matcher.group(3);
			Object listObject = context.get(listName);

			if (listObject instanceof Iterable<?> list) {
				StringBuilder repeatedContent = new StringBuilder();
				for (Object item : list) {
					Map<String, Object> itemContext = new java.util.HashMap<>(context);
					itemContext.put(variableName, item);
					String renderedContent = renderObjectVariables(content, itemContext); // 객체의 속성 렌더링
					repeatedContent.append(render(renderedContent, itemContext));
				}
				matcher.appendReplacement(sb, repeatedContent.toString());
			} else {
				matcher.appendReplacement(sb, "");
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
