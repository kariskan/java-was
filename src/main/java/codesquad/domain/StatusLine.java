package codesquad.domain;

import static codesquad.utils.StringUtils.lineSeparator;

public record StatusLine(
	HttpProtocol protocol,
	HttpStatus status
) {
	public static StatusLine ok() {
		return new StatusLine(HttpProtocol.HTTP11, HttpStatus.OK);
	}

	@Override
	public String toString() {
		return protocol.getProtocol() + " " + status.getCode() + " " + status.getValue() + lineSeparator();
	}
}
