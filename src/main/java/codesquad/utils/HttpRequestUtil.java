package codesquad.utils;

import static codesquad.utils.StringUtils.lineSeparator;

import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import codesquad.domain.Path;
import codesquad.domain.RequestLine;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final int MAX_HEADER_SIZE = 8 * (1 << 10);
	private static final int MAX_BODY_SIZE = 2 * (1 << 10) * (1 << 10);
	private static final String SPACE_DELIMITER = " ";
	private static final String HEADER_DELIMITER = ": ";

	private HttpRequestUtil() {
	}

	public static HttpRequest parseRequest(InputStream bi) {
		try {
			String[] split = splitHeaders(bi);

			RequestLine requestLine = parseRequestLine(split[0]);
			HttpHeader httpHeader = parseHeader(split);
			String requestBody = "";
			if (httpHeader.existsHeaderValue(CONTENT_LENGTH)) {
				requestBody = parseBody(Integer.parseInt(httpHeader.getHeaderValue(CONTENT_LENGTH)), bi);
			}

			HttpRequest request = new HttpRequest(requestLine, httpHeader, requestBody);
			log.debug("{}", request);
			return request;
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed to parse request", e);
		}
	}

	private static String[] splitHeaders(InputStream bi) throws IOException {
		String[] split = getHeaderStrings(bi);
		if (split.length != 1) {
			throw new IllegalArgumentException("max request bytes exceeded");
		}
		split = split[0].split(lineSeparator());
		return split;
	}

	private static String[] getHeaderStrings(InputStream bi) throws IOException {
		int idx = 0;
		byte[] bytes = new byte[MAX_HEADER_SIZE];
		while (idx < MAX_HEADER_SIZE) {
			int read = bi.read();
			bytes[idx++] = (byte) read;
			if (checkHeaderEnd(idx, bytes)) {
				break;
			}
		}
		String doubleLineSeparator = lineSeparator() + lineSeparator();
		return new String(bytes, 0, idx).split(doubleLineSeparator);
	}

	private static boolean checkHeaderEnd(int idx, byte[] bytes) {
		return idx >= 4 && bytes[idx - 4] == '\r' && bytes[idx - 3] == '\n' && bytes[idx - 2] == '\r'
			&& bytes[idx - 1] == '\n';
	}

	private static RequestLine parseRequestLine(String line) throws IOException {
		String[] split = line.split(SPACE_DELIMITER);

		if (split.length != 3) {
			throw new IllegalArgumentException("Invalid request line");
		}

		HttpMethod httpMethod = HttpMethod.from(split[0]);
		HttpProtocol httpProtocol = HttpProtocol.from(split[2]);
		return new RequestLine(httpMethod, new Path(split[1]), httpProtocol);
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

	private static String parseBody(int length, InputStream bi) throws IOException {
		if (length > MAX_BODY_SIZE) {
			throw new IllegalArgumentException("max request bytes exceeded");
		}
		byte[] bytes = new byte[length];
		int idx = 0;
		while (length-- > 0) {
			bytes[idx++] = (byte) bi.read();
		}
		return new String(bytes, 0, idx);
	}
}
