package codesquad.domain;

public record HttpResponse(
	StatusLine statusLine,
	HttpHeader header,
	String body
) {
}
