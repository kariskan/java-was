package codesquad.handler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.domain.ContentType;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.StatusLine;

public class StaticResourceHandler implements Handler {

	public static final String STATIC_PATH = "static";
	private final HttpRequest httpRequest;
	private final Logger log = LoggerFactory.getLogger(StaticResourceHandler.class);

	public StaticResourceHandler(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	@Override
	public HttpResponse doService() {
		try {
			URL resource = getClass().getClassLoader().getResource(STATIC_PATH + httpRequest.requestLine().url());
			if (resource == null) {
				throw new IllegalArgumentException("Resource not found: " + httpRequest.requestLine());
			}
			File file = new File(resource.getFile());
			byte[] bytes = Files.readAllBytes(file.toPath());

			HttpHeader httpHeader = makeHttpHeader(file);
			return new HttpResponse(StatusLine.ok(), httpHeader, bytes);
		} catch (IOException e) {
			log.error("io exception", e);
			return null;
		}
	}

	private HttpHeader makeHttpHeader(File file) {
		HttpHeader httpHeader = HttpHeader.of();
		httpHeader.setHeaderValue("Content-Length", String.valueOf(file.length()));
		httpHeader.setHeaderValue("Content-Type", ContentType.from(httpRequest.getExtension()).getMimeType());
		return httpHeader;
	}
}
