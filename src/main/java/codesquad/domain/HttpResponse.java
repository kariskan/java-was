package codesquad.domain;

public record HttpResponse(
	StatusLine statusLine,
	HttpHeader header,
	byte[] body
) {

	@Override
	public String toString() {
		return statusLine.toString() + header.toString() + new String(body);
	}

	public byte[] getBytes() {
		return toString().getBytes();
	}
}
