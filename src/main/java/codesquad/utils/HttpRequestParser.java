package codesquad.utils;

import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import codesquad.domain.RequestLine;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

	private static final String CONTENT_LENGTH = "Content-Length";
	public static final String LINE_SEPARATOR = "\r\n";
	private static final int MAX_REQUEST_BYTES = 2 * (1 << 10) * (1 << 10);
	private static final int BUFFER_SIZE = 8 * (1 << 10);

	private HttpRequestParser() {
	}

	private static final String SPACE_DELIMITER = " ";
	private static final String HEADER_DELIMITER = ": ";

	public static HttpRequest parseRequest(BufferedInputStream bi) {
		RequestLine requestLine;
		HttpHeader httpHeader;
		String requestBody = "";

		try {
			byte[] bytes = new byte[MAX_REQUEST_BYTES];
			int idx = 0;
			while (idx == 0 || (idx < MAX_REQUEST_BYTES && bytes[idx] != 0)) {
				bi.read(bytes, idx, BUFFER_SIZE);
				idx += BUFFER_SIZE;
			}
			String doubleLineSeparator = LINE_SEPARATOR + LINE_SEPARATOR;
			String[] split = new String(bytes).split(doubleLineSeparator);
			if (split.length != 2) {
				throw new IllegalArgumentException("max request bytes exceeded");
			}

			String[] preSplit = split[0].split(LINE_SEPARATOR);
			requestLine = parseRequestLine(preSplit[0]);
			httpHeader = parseHeader(Arrays.copyOfRange(preSplit, 1, preSplit.length));
			if (httpHeader.existsHeaderValue(CONTENT_LENGTH)) {
				requestBody = parseBody(Integer.parseInt(httpHeader.getHeaderValue(CONTENT_LENGTH)), split[1]);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed to parse request", e);
		}

		return new HttpRequest(requestLine, httpHeader, requestBody);
	}

	private static RequestLine parseRequestLine(String line) throws IOException {
		String[] split = line.split(SPACE_DELIMITER);

		if (split.length != 3) {
			throw new IllegalArgumentException("Invalid request line");
		}

		HttpMethod httpMethod = HttpMethod.from(split[0]);
		String url = split[1];
		HttpProtocol httpProtocol = HttpProtocol.from(split[2]);
		return new RequestLine(httpMethod, url, httpProtocol);
	}

	private static HttpHeader parseHeader(String[] split) throws IOException {
		Map<String, String> headers = new HashMap<>();
		for (int i = 1; i < split.length; i++) {
			String[] header = split[i].split(HEADER_DELIMITER);
			if (header.length != 2) {
				throw new IllegalArgumentException("Invalid header line");
			}
			headers.put(header[0], header[1]);
		}
		return new HttpHeader(headers);
	}

	private static String parseBody(int length, String s) throws IOException {
		return s.substring(0, length);
	}
}
