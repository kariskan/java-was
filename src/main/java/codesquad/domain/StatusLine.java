package codesquad.domain;

public record StatusLine(
	HttpProtocol protocol,
	HttpStatus status
) {
}
