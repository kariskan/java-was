package codesquad.domain;

public record HttpRequest(
	RequestLine requestLine,
	HttpHeader header,
	String body
) {

	public String getExtension() {
		String[] paths = requestLine.url().split("\\.");
		return paths[paths.length - 1];
	}
}
