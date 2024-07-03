package codesquad.handler;

import codesquad.domain.ContentType;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.StatusLine;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;

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
//			 TODO http fail response
			if (resource == null) {
				throw new IllegalArgumentException("Resource not found: " + httpRequest.requestLine());
			}
			HashMap<String, String> headers = new HashMap<>();
			File file = new File(resource.getFile());
			byte[] bytes = Files.readAllBytes(file.toPath());

			headers.put("Content-Length", String.valueOf(file.length()));
			headers.put("Content-Type", ContentType.from(httpRequest.getExtension()).getMimeType());
			return new HttpResponse(StatusLine.ok(), new HttpHeader(headers), bytes);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not load resource", e);
		}
	}
}
