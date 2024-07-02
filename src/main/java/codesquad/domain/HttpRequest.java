package codesquad.domain;

public record HttpRequest(
	RequestLine requestLine,
	HttpHeader header,
	String body
) {

	public boolean isGet() {
		return requestLine.method() == HttpMethod.GET;
	}

	public boolean isEqualsPath(String path) {
		return requestLine.url().equals(path);
	}

	public HttpProtocol getProtocol() {
		return requestLine.protocol();
	}
}
