package codesquad.domain;

import static codesquad.utils.StringUtils.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpHeader {

	private final Map<String, String> headers;

	public HttpHeader(Map<String, String> headers) {
		this.headers = headers;
	}

	public static HttpHeader of() {
		HttpHeader httpHeader = new HttpHeader(new HashMap<>());
		httpHeader.setDefaultHeaders();
		return httpHeader;
	}

	public boolean existsHeaderValue(String headerName) {
		return headers.containsKey(headerName);
	}

	public String getHeaderValue(String headerName) {
		return headers.get(headerName);
	}

	public void setHeaderValue(String headerName, String headerValue) {
		headers.put(headerName, headerValue);
	}

	public void setDefaultHeaders() {
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.of("GMT"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		headers.put("Date", zonedDateTime.format(formatter));
	}

	@Override
	public String toString() {
		return headers.entrySet().stream()
				   .map(e -> e.getKey() + ": " + e.getValue())
				   .collect(Collectors.joining(lineSeparator())) + lineSeparator() + lineSeparator();
	}
}
