package codesquad.utils;

import static codesquad.utils.StringUtils.lineSeparator;

import codesquad.domain.Cookie;
import codesquad.domain.HttpBody;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpStatus;
import codesquad.domain.Path;
import codesquad.domain.RequestLine;
import codesquad.error.BaseException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
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

	public static HttpRequest parseRequest(InputStream bi) throws IOException {
		String[] split = splitHeaders(bi);

		RequestLine requestLine = parseRequestLine(split[0]);
		HttpHeader httpHeader = parseHeader(split);
		HttpBody body = null;
		if (httpHeader.existsHeaderValue(CONTENT_LENGTH)) {
			body = parseBody(Integer.parseInt(httpHeader.getHeaderValue(CONTENT_LENGTH)), bi);
		}
		Cookie[] cookies = parseCookies(httpHeader);
		HttpRequest request = new HttpRequest(requestLine, httpHeader, body, cookies);
		log.debug("{}", request);
		return request;
	}

	private static Cookie[] parseCookies(HttpHeader header) {
		String cookie = header.getHeaderValue("Cookie");
		if (cookie == null || cookie.isEmpty()) {
			return null;
		}
		cookie = cookie.replaceAll(" ", "");
		String[] cookieSplit = cookie.split(";");
		Cookie[] res = new Cookie[cookieSplit.length];
		for (int i = 0; i < cookieSplit.length; i++) {
			String[] segment = cookieSplit[i].split("=");
			res[i] = new Cookie(segment[0], segment[1], null, null);
		}
		return res;
	}

	private static String[] splitHeaders(InputStream bi) throws IOException {
		String[] split = getHeaderStrings(bi);
		if (split.length != 1) {
			throw new BaseException(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE, "max request bytes exceeded");
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
			throw new BaseException(HttpStatus.BAD_REQUEST, "Invalid request line");
		}

		HttpMethod httpMethod = HttpMethod.from(split[0]);
		HttpProtocol httpProtocol = HttpProtocol.from(split[2]);
		return new RequestLine(httpMethod, new Path(URLDecoder.decode(split[1], "UTF-8")), httpProtocol);
	}

	private static HttpHeader parseHeader(String[] split) throws IOException {
		Map<String, String> headers = new HashMap<>();
		for (int i = 1; i < split.length; i++) {
			String[] header = split[i].split(HEADER_DELIMITER);
			if (header.length != 2) {
				throw new BaseException(HttpStatus.BAD_REQUEST, "Invalid header line");
			}
			String headerValue = header[1];
			headers.put(header[0], headerValue);
		}
		return new HttpHeader(headers);
	}

	private static HttpBody parseBody(int length, InputStream bi) throws IOException {
		if (length > MAX_BODY_SIZE) {
			throw new BaseException(HttpStatus.PAYLOAD_TOO_LARGE, "max request bytes exceeded");
		}
		return new HttpBody(bi.readNBytes(length));
	}
}
