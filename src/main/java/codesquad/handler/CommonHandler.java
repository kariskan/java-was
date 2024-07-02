package codesquad.handler;

import codesquad.domain.HttpHeader;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.HttpStatus;
import codesquad.domain.StatusLine;
import java.util.Map;

public class CommonHandler implements Handler {

	@Override
	public HttpResponse doService(HttpRequest request) {
		if (request.isGet() && request.isEqualsPath("index.html")) {
			return doIndex(request);
		}
		return null;
	}

	private HttpResponse doIndex(HttpRequest request) {
		return new HttpResponse(new StatusLine(request.getProtocol(), HttpStatus.OK), new HttpHeader(Map.of()), "index.html");
	}
}
