package codesquad.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.annotation.RequestMapping;
import codesquad.db.UserDatabase;
import codesquad.domain.ContentType;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.HttpStatus;
import codesquad.domain.RequestLine;
import codesquad.domain.User;
import codesquad.error.BaseException;
import codesquad.utils.UserThreadLocal;

public class StaticRequestHandler {

	public static final String STATIC_PATH = "static";
	private final UserDatabase userDatabase = UserDatabase.getInstance();

	private final Logger log = LoggerFactory.getLogger(StaticRequestHandler.class);

	private StaticRequestHandler() {
	}

	@RequestMapping(httpMethod = HttpMethod.GET, url = "/static")
	public void doService(HttpRequest request, HttpResponse response) {
		try {
			byte[] body = readBytesFromFile(request.requestLine().getUrl());
			body = applyDynamicHeaderComponents(body);
			if (request.isGet() && request.getUrl().equals("/user/list.html")) {
				if (!UserThreadLocal.isLogin()) {
					response.sendRedirect("/user/login.html");
					return;
				}
				body = applyUserListHtml(body);
			}
			if (request.isGet() && request.getUrl().equals("/error.html")) {
				body = applyErrorPlaceHolder(body, request);
				response.setStatusLine(HttpStatus.from(request.getParameters().getValueByKey("statusCode").split(" ")[0]));
				response.setBody(body);
				response.setHeader(makeHttpHeader(body.length, request));
				return;
			}
			HttpHeader header = makeHttpHeader(body.length, request);
			setResponse(response, header, body);
		} catch (IOException e) {
			log.error("io exception", e);
		}
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

	private byte[] applyDynamicHeaderComponents(byte[] body) throws IOException {
		String html = new String(body);
		String headerFile = UserThreadLocal.isLogin() ? "/login_header.html" : "/logout_header.html";
		byte[] headerBytes = readBytesFromFile(headerFile);
		String headerHtml = new String(headerBytes);

		if (UserThreadLocal.isLogin()) {
			String nickname = UserThreadLocal.get().nickname();
			headerHtml = headerHtml.replace("{{nickname}}", nickname);
		}

		html = html.replace("<div class=\"container\">", "<div class=\"container\">" + headerHtml);
		return html.getBytes();
	}

	private byte[] applyUserListHtml(byte[] body) {
		String templateHtml = new String(body);

		List<User> users = userDatabase.findAll();
		StringBuilder userListHtml = new StringBuilder();
		for (User user : users) {
			userListHtml.append("<tr>");
			userListHtml.append("<td>").append(user.userId()).append("</td>");
			userListHtml.append("<td>").append(user.nickname()).append("</td>");
			userListHtml.append("<td>").append(user.email()).append("</td>");
			userListHtml.append("</tr>");
		}

		templateHtml = templateHtml.replace("<!-- USER_LIST_PLACEHOLDER -->", userListHtml.toString());
		return templateHtml.getBytes();
	}

	private byte[] applyErrorPlaceHolder(byte[] body, HttpRequest request) {
		String statusCode = request.getParameters().getValueByKey("statusCode");
		String message = request.getParameters().getValueByKey("message");
		String templateHtml = new String(body);

		templateHtml = templateHtml.replace("{{statusCode}}", statusCode);
		templateHtml = templateHtml.replace("{{message}}", message);
		return templateHtml.getBytes();
	}
}
