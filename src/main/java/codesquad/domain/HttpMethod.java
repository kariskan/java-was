package codesquad.domain;

import java.util.Arrays;

import codesquad.error.BaseException;

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
