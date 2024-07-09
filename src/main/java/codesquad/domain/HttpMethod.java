package codesquad.domain;

import codesquad.error.BaseException;
import java.util.Arrays;

public enum HttpMethod {

	GET,
	HEAD,
	POST,
	PUT,
	DELETE,
	CONNECT,
	OPTIONS,
	TRACE,
	PATCH;

	public static HttpMethod from(String method) {
		return Arrays.stream(values())
			.filter(httpMethod -> httpMethod.name().equals(method))
			.findFirst()
			.orElseThrow(() -> new BaseException(HttpStatus.METHOD_NOT_ALLOWED, "unknown method: " + method));
	}
}
