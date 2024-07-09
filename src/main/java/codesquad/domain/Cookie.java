package codesquad.domain;

import java.time.Duration;

public class Cookie {

	private final String name;
	private final String value;
	private final Duration maxAge;
	private final String path;

	public Cookie(String name, String value, Duration maxAge, String path) {
		this.name = name;
		this.value = value;
		this.maxAge = maxAge;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public Duration getMaxAge() {
		return maxAge;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("=").append(value);
		if (maxAge != null) {
			sb.append("; Max-Age=").append(maxAge.toMillis());
		}
		if (path != null) {
			sb.append("; Path=").append(path);
		}
		return sb.toString();
	}
}
