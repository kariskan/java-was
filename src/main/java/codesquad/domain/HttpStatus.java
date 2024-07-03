package codesquad.domain;

public enum HttpStatus {

	OK(200),

	BAD_REQUEST(400),
	UNAUTHORIZED(401),
	FORBIDDEN(403),
	NOT_FOUND(404),
	PAYLOAD_TOO_LARGE(413),

	INTERNAL_SERVER_ERROR(500);

	private final int code;

	HttpStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
