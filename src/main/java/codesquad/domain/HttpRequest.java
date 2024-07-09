package codesquad.domain;

import codesquad.error.BaseException;
import java.util.Arrays;

public record HttpRequest(
	RequestLine requestLine,
	HttpHeader header,
	HttpBody body,
	Cookie[] cookies
) {

	public String getExtension() {
		String[] paths = requestLine.getUrl().split("\\.");
		return "." + paths[paths.length - 1];
	}

	public boolean isGet() {
		return requestLine.method() == HttpMethod.GET;
	}

	public boolean isPost() {
		return requestLine.method() == HttpMethod.POST;
	}

	public boolean isStaticRequest() {
		return Arrays.stream(ContentType.values())
			.anyMatch(contentType -> requestLine.getUrl().split("&")[0].endsWith(contentType.getExtension()));
	}

	public Parameters getParameters() {
		return requestLine.path().getParameters();
	}

	public Cookie getCookie(String name) {
		if (cookies == null || cookies.length == 0) {
			throw new BaseException(HttpStatus.BAD_REQUEST, "cookie not found");
		}
		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(name))
			.findFirst()
			.orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST, "cookie not found"));
	}
}
