package codesquad.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.data.UploadFile;
import codesquad.error.BaseException;

public record HttpBody(byte[] body) {

	private static final Logger log = LoggerFactory.getLogger(HttpBody.class);

	public Map<String, String> bodyToMap() {
		return Arrays.stream(new String(body).split("&"))
			.map(s -> s.split("="))
			.collect(Collectors.toMap(split -> split[0], split -> {
				try {
					return URLDecoder.decode(split[1], "UTF-8");
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage());
					throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
				}
			}));
	}

	public Map<String, Object> bodyToMultipart(String boundary) {
		Map<String, Object> multipartData = new HashMap<>();
		String boundaryString = "--" + boundary;
		byte[] boundaryBytes;
		try {
			boundaryBytes = boundaryString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
		}

		int start = 0;
		int end = indexOf(body, boundaryBytes, start);

		while (end != -1) {
			int nextStart = indexOf(body, boundaryBytes, end + boundaryBytes.length);
			if (nextStart == -1)
				break;

			int partStart = end + boundaryBytes.length + 2; // Skip boundary and \r\n
			int partEnd = nextStart - 2; // Exclude the \r\n before the next boundary

			byte[] part = new byte[partEnd - partStart];
			System.arraycopy(body, partStart, part, 0, part.length);

			try {
				parsePart(part, multipartData);
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage());
				throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
			}

			start = nextStart;
			end = nextStart;
		}

		return multipartData;
	}

	private void parsePart(byte[] part, Map<String, Object> multipartData) throws UnsupportedEncodingException {
		int headerEnd = indexOf(part, "\r\n\r\n".getBytes("UTF-8"), 0);
		if (headerEnd == -1)
			return;

		byte[] headerBytes = new byte[headerEnd];
		System.arraycopy(part, 0, headerBytes, 0, headerBytes.length);
		String headerString = new String(headerBytes, "UTF-8");
		String[] headers = headerString.split("\r\n");

		String contentDisposition = null;
		for (String header : headers) {
			if (header.startsWith("Content-Disposition: form-data;")) {
				contentDisposition = header;
				break;
			}
		}

		if (contentDisposition == null)
			return;

		String[] cdParts = contentDisposition.split(";");
		String name = null;
		String fileName = null;

		for (String cdPart : cdParts) {
			cdPart = cdPart.trim();
			if (cdPart.startsWith("name=")) {
				name = cdPart.substring("name=".length()).replace("\"", "");
			} else if (cdPart.startsWith("filename=")) {
				fileName = cdPart.substring("filename=".length()).replace("\"", "");
			}
		}

		int dataStart = headerEnd + 4; // Skip the \r\n\r\n
		byte[] dataBytes = new byte[part.length - dataStart];
		System.arraycopy(part, dataStart, dataBytes, 0, dataBytes.length);

		if (fileName != null) {
			multipartData.put(name, new UploadFile(null, fileName, dataBytes));
		} else {
			try {
				multipartData.put(name, new String(dataBytes, "UTF-8").trim());
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage());
				throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
			}
		}
	}

	private int indexOf(byte[] array, byte[] target, int start) {
		outer:
		for (int i = start; i <= array.length - target.length; i++) {
			for (int j = 0; j < target.length; j++) {
				if (array[i + j] != target[j]) {
					continue outer;
				}
			}
			return i;
		}
		return -1;
	}
}
