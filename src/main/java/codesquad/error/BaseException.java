package codesquad.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.domain.HttpStatus;

public class BaseException extends RuntimeException {

	private static final Logger log = LoggerFactory.getLogger(BaseException.class);

	private final HttpStatus status;
	private final String message;

	public BaseException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public static BaseException serverException(Exception e) {
		log.error(e.getMessage());
		return new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	public static BaseException serverException() {
		return new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
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
