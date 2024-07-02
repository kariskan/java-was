package codesquad.domain;

public enum HttpStatus {

	OK(200);

	private final int code;

	HttpStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
