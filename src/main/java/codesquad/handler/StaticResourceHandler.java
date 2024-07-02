package codesquad.handler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import codesquad.domain.HttpHeader;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.StatusLine;

public class StaticResourceHandler implements Handler {

	public static final String STATIC_PATH = "static";
	private final HttpRequest httpRequest;

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
			BufferedInputStream inputStream = new BufferedInputStream(resource.openStream());
			return new HttpResponse(StatusLine.ok(), new HttpHeader(Map.of()), new String(inputStream.readAllBytes()));
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not load resource", e);
		}
	}
}
