package codesquad.domain;

public class HttpResponse {

	private StatusLine statusLine;
	private HttpHeader header;
	private HttpBody body;

	public HttpResponse() {
	}

	public HttpResponse(StatusLine statusLine, HttpHeader header, HttpBody body) {
		this.statusLine = statusLine;
		this.header = header;
		this.body = body;
	}

	@Override
	public String toString() {
		return statusLine.toString() + header.toString() + new String(body.body());
	}

	public byte[] getBytes() {
		return toString().getBytes();
	}

	public byte[] getBody() {
		return body.body();
	}

	public void setBody(byte[] body) {
		this.body = new HttpBody(body);
	}

	public HttpHeader getHeader() {
		return header;
	}

	public void setHeader(HttpHeader header) {
		this.header = header;
	}

	public StatusLine getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(HttpStatus httpStatus) {
		this.statusLine = new StatusLine(HttpProtocol.HTTP11, httpStatus);
	}

	public void setStatusLine() {
		this.statusLine = new StatusLine(HttpProtocol.HTTP11, HttpStatus.OK);
	}
}
