package codesquad.domain;

public enum HttpStatus {

	OK(200, "OK"),

	MOVED_PERMANENTLY(301, "Moved Permanently"),
	FOUND(302, "FOUND"),

	BAD_REQUEST(400, "Bad Request"),
	UNAUTHORIZED(401, "Unauthorized"),
	FORBIDDEN(403, "Forbidden"),
	NOT_FOUND(404, "Not Found"),
	METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
	PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),

	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	;

	private final int code;
	private final String value;

	HttpStatus(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}


	@Override
	public String toString() {
		return code + " " + value;
	}
}
