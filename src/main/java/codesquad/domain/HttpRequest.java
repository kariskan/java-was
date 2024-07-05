package codesquad.domain;

import java.util.Arrays;

public record HttpRequest(
	RequestLine requestLine,
	HttpHeader header,
	String body
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
}
