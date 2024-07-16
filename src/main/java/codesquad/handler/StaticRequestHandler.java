package codesquad.handler;

import codesquad.annotation.RequestMapping;
import codesquad.data.User;
import codesquad.db.UserDatabase;
import codesquad.domain.ContentType;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;
import codesquad.utils.TemplateEngine;
import codesquad.utils.UserThreadLocal;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticRequestHandler {

	public static final String STATIC_PATH = "static";
	private final UserDatabase userDatabase = UserDatabase.getInstance();
	private final Logger log = LoggerFactory.getLogger(StaticRequestHandler.class);

	private StaticRequestHandler() {
	}

	@RequestMapping(httpMethod = HttpMethod.GET, url = "/static")
	public void doService(HttpRequest request, HttpResponse response) {
		try {
			byte[] body = applyDynamicComponents(request, response);
			if (body == null) {
				return;
			}
			HttpHeader header = makeHttpHeader(body.length, request);
			setResponse(response, header, body);
		} catch (IOException e) {
			log.error("io exception", e);
		}
	}

	private byte[] applyDynamicComponents(HttpRequest request, HttpResponse response) throws IOException {
		byte[] body = readBytesFromFile(request.requestLine().getUrl());
		Map<String, Object> context = new HashMap<>();
//		body = applyDynamicHeaderComponents(body);
		if (request.isGet() && request.getUrl().equals("/user/list.html")) {
			context.put("users", userDatabase.findAll());
		}
		if (UserThreadLocal.isLogin()) {
			context.put("nickname", UserThreadLocal.get().userId());
			context.put("isLogin", true);
		}
		if (request.isGet() && request.getUrl().equals("/error.html")) {
			setErrorResponse(request, response, body, context);
			return null;
		}
		return TemplateEngine.render(new String(body), context).getBytes();
	}

	private void setErrorResponse(HttpRequest request, HttpResponse response, byte[] body,
								  Map<String, Object> context) {
		context.put("statusCode", request.getParameters().getValueByKey("statusCode"));
		context.put("message", request.getParameters().getValueByKey("message"));
		String html = TemplateEngine.render(new String(body), context);
		body = html.getBytes();
		response.setStatusLine(HttpStatus.from(request.getParameters().getValueByKey("statusCode").split(" ")[0]));
		response.setBody(body);
		response.setHeader(makeHttpHeader(body.length, request));
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

	private byte[] readBytesFromFile(String source) throws IOException {
		URL resource = getClass().getClassLoader().getResource(STATIC_PATH + source);
		if (resource == null) {
			throw new BaseException(HttpStatus.NOT_FOUND, "Resource not found: " + source);
		}
		try (InputStream inputStream = resource.openStream()) {
			return inputStream.readAllBytes();
		}
	}
}
