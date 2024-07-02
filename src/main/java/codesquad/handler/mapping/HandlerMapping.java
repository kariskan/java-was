package codesquad.handler.mapping;

import codesquad.domain.HttpRequest;
import codesquad.handler.Handler;
import codesquad.handler.StaticResourceHandler;

public class HandlerMapping {

	private HandlerMapping() {
	}

	public static Handler getHandler(HttpRequest request) {
		// if (request.isGet() && request.isStaticRequest()) {
		return new StaticResourceHandler(request);
		// }
	}
}
