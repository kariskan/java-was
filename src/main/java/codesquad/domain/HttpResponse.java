package codesquad.domain;

public record HttpResponse(
	StatusLine statusLine,
	HttpHeader header,
	String body
) {

	@Override
	public String toString() {
		return statusLine.toString() + header.toString() + body;
	}
}
