package codesquad.domain;

public record HttpRequest(
	RequestLine requestLine,
	HttpHeader header,
	String body
) {
}
