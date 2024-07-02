package codesquad.domain;

import java.util.Arrays;

public enum HttpProtocol {

	HTTP11("HTTP/1.1");

	private final String protocol;

	HttpProtocol(final String protocol) {
		this.protocol = protocol;
	}

	public static HttpProtocol from(String protocol) {
		return Arrays.stream(values())
			.filter(httpProtocol -> httpProtocol.protocol.equals(protocol))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown protocol: " + protocol));
	}

	public String getProtocol() {
		return protocol;
	}
}
