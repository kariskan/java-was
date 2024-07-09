package codesquad.domain;

import java.util.HashMap;

public class HttpResponse {

	private StatusLine statusLine;
	private HttpHeader header;
	private HttpBody body;

	public HttpResponse() {
		header = new HttpHeader(new HashMap<>());
		header.setDefaultHeaders();
	}

	@Override
	public String toString() {
		return statusLine.toString() + header.toString() + new String(body.body());
	}

	public byte[] getBody() {
		if (body == null) {
			return null;
		}
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

	public void addHeader(String name, String value) {
		header.setHeaderValue(name, value);
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

	public void sendRedirect(String location) {
		setStatusLine(HttpStatus.FOUND);
		if (header == null) {
			header = HttpHeader.of();
		}
		header.setHeaderValue("Location", location);
	}
}
