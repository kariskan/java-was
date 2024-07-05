package codesquad.domain;

public class HttpResponse {

	private StatusLine statusLine;
	private HttpHeader header;
	private byte[] body;

	public HttpResponse() {
	}

	public HttpResponse(StatusLine statusLine, HttpHeader header, byte[] body) {
		this.statusLine = statusLine;
		this.header = header;
		this.body = body;
	}

	@Override
	public String toString() {
		return statusLine.toString() + header.toString() + new String(body);
	}

	public byte[] getBytes() {
		return toString().getBytes();
	}

	public byte[] getBody() {
		return body;
	}

	public HttpHeader getHeader() {
		return header;
	}

	public StatusLine getStatusLine() {
		return statusLine;
	}

	public void setHeader(HttpHeader header) {
		this.header = header;
	}

	public void setStatusLine() {
		this.statusLine = new StatusLine(HttpProtocol.HTTP11, HttpStatus.OK);
	}

	public void setStatusLine(HttpStatus httpStatus) {
		this.statusLine = new StatusLine(HttpProtocol.HTTP11, httpStatus);
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
}
