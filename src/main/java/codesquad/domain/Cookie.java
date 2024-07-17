package codesquad.domain;

import java.time.Duration;

public record Cookie(
	String name,
	String value,
	Duration maxAge,
	String path
) {

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
