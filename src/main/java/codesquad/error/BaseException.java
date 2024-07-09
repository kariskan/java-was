package codesquad.error;

import codesquad.domain.HttpStatus;

public class BaseException extends RuntimeException {

	private final HttpStatus status;
	private final String message;

	public BaseException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
