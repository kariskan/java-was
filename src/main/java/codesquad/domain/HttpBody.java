package codesquad.domain;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record HttpBody(byte[] body) {

	public Map<String, String> bodyToMap() {
		return Arrays.stream(new String(body).split("&"))
			.map(s -> s.split("="))
			.collect(Collectors.toMap(split -> split[0], split -> URLDecoder.decode(split[1], StandardCharsets.UTF_8)));
	}
}
