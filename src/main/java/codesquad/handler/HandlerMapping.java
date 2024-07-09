package codesquad.handler;

import codesquad.domain.HttpRequest;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;
import java.net.URL;

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
		if (!request.isGet() || resource == null) {
			return getDynamicHandler(request);
		}
		return StaticRequestHandler.getInstance();
	}

	public static Handler getDynamicHandler(HttpRequest request) {
		if (request.isPost() && "/create".equals(request.requestLine().getUrl())) {
			return SignUpHandler.getInstance();
		}
		if (request.isPost() && "/login".equals(request.requestLine().getUrl())) {
			return LoginHandler.getInstance();
		}
		if (request.isPost() && "/logout".equals(request.requestLine().getUrl())) {
			return LogoutHandler.getInstance();
		}
		throw new BaseException(HttpStatus.NOT_FOUND, "Invalid request");
	}
}
