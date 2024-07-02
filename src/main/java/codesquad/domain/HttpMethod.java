package codesquad.domain;

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
			.orElseThrow(() -> new IllegalArgumentException("unknown method: " + method));
	}
}
