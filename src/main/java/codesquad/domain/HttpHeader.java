package codesquad.domain;

import static codesquad.utils.StringUtils.*;

import java.util.Map;
import java.util.stream.Collectors;

public record HttpHeader(
	Map<String, String> headers
) {

	public boolean existsHeaderValue(String headerName) {
		return headers.containsKey(headerName);
	}

	public String getHeaderValue(String headerName) {
		return headers.get(headerName);
	}

	@Override
	public String toString() {
		return headers.entrySet().stream()
				   .map(e -> e.getKey() + ": " + e.getValue())
				   .collect(Collectors.joining(lineSeparator())) + lineSeparator() + lineSeparator();
	}
}
