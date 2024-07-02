package codesquad.domain;

import java.util.Map;

public record HttpHeader(
	Map<String, String> headers
) {

	public boolean existsHeaderValue(String headerName) {
		return headers.containsKey(headerName);
	}

	public String getHeaderValue(String headerName) {
		return headers.get(headerName);
	}
}
