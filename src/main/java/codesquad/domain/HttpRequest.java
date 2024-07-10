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
			return null;
		}
		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(name))
			.findFirst()
			.orElse(null);
	}

	public String getUrl() {
		return requestLine.getUrl();
	}

	@Override
	public String toString() {
		return "HttpRequest{" +
			"requestLine=" + requestLine +
			", header=" + header +
			", body=" + body +
			", cookies=" + Arrays.toString(cookies) +
			'}';
	}
}
