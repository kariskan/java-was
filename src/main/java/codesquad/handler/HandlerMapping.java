package codesquad.handler;

import codesquad.domain.HttpMethod;
import codesquad.domain.HttpProtocol;
import codesquad.domain.HttpRequest;
import codesquad.domain.Path;
import codesquad.domain.RequestLine;
import java.util.Map;
import java.util.Map.Entry;

public class HandlerMapping {

//	private static final Map<String, Handler> DYNAMIC_HANDLERS = Map.of(
//		new RequestLine(HttpMethod.GET, new Path("/create"), HttpProtocol.HTTP11), SignUpHandler.getInstance()
//	);
//	private static final Handler STATIC_HANDLER = StaticRequestHandler.getInstance();

	private HandlerMapping() {
	}

	public static Handler getHandler(HttpRequest request) {
		if (request.isGet() && "/create".equals(request.requestLine().getUrl())) {
			return SignUpHandler.getInstance();
		}
		return StaticRequestHandler.getInstance();
	}
}
