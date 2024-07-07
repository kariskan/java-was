package codesquad.handler;

import java.net.URL;

import codesquad.domain.HttpRequest;

public class HandlerMapping {

	public static final String STATIC_PATH = "static";

//	private static final Map<String, Handler> DYNAMIC_HANDLERS = Map.of(
//		new RequestLine(HttpMethod.GET, new Path("/create"), HttpProtocol.HTTP11), SignUpHandler.getInstance()
//	);
//	private static final Handler STATIC_HANDLER = StaticRequestHandler.getInstance();

	private HandlerMapping() {
	}

	public static Handler getHandler(HttpRequest request) {
		URL resource = HandlerMapping.class.getClassLoader().getResource(STATIC_PATH + request.requestLine().getUrl());
		if (resource == null) {
			return getDynamicHandler(request);
		}
		return StaticRequestHandler.getInstance();
	}

	public static Handler getDynamicHandler(HttpRequest request) {
		if (request.isGet() && "/create".equals(request.requestLine().getUrl())) {
			return SignUpHandler.getInstance();
		}
		throw new IllegalArgumentException("Invalid request");
	}
}
