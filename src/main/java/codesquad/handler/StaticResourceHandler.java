package codesquad.handler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
			Path path = Path.of(resource.toURI());
			byte[] bytes = Files.readAllBytes(path);
			String contentType = Files.probeContentType(path);
			return new HttpResponse(StatusLine.ok(), new HttpHeader(Map.of("Content-Type", contentType)), new String(bytes));
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not load resource", e);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Could not parse resource", e);
		}
	}
}
