package codesquad.domain;

public record RequestLine(
	HttpMethod method,
	String url,
	HttpProtocol protocol
) {
}
