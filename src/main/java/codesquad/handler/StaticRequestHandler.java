package codesquad.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.domain.ContentType;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;

public class StaticRequestHandler implements Handler {

	public static final String STATIC_PATH = "static";
	private static final StaticRequestHandler instance = new StaticRequestHandler();

	private final Logger log = LoggerFactory.getLogger(StaticRequestHandler.class);

	private StaticRequestHandler() {
	}

	public static StaticRequestHandler getInstance() {
		return instance;
	}

	@Override
	public void doService(HttpRequest request, HttpResponse response) {
		try {
			byte[] body = readBytesFromFile(request);
			HttpHeader header = makeHttpHeader(body.length, request);
			setResponse(response, header, body);
		} catch (IOException e) {
			log.error("io exception", e);
		}
	}

	private void setResponse(HttpResponse response, HttpHeader header, byte[] body) {
		response.setStatusLine();
		response.setHeader(header);
		response.setBody(body);
	}

	private HttpHeader makeHttpHeader(long length, HttpRequest request) {
		HttpHeader httpHeader = HttpHeader.of();
		httpHeader.setHeaderValue("Content-Length", String.valueOf(length));
		httpHeader.setHeaderValue("Content-Type", ContentType.from(request.getExtension()).getMimeType());
		return httpHeader;
	}

	private byte[] readBytesFromFile(HttpRequest request) throws IOException {
		URL resource = getClass().getClassLoader().getResource(STATIC_PATH + request.requestLine().getUrl());
		if (resource == null) {
			throw new IllegalArgumentException("Resource not found: " + request.requestLine());
		}
		try (InputStream inputStream = resource.openStream()) {
			return inputStream.readAllBytes();
		}
	}
}
