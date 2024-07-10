package codesquad.handler;

import codesquad.db.SessionDatabase;
import codesquad.db.UserDatabase;
import codesquad.domain.Cookie;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.HttpStatus;
import codesquad.domain.User;
import codesquad.error.BaseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginHandler extends DynamicHandler {

	private static final LoginHandler instance = new LoginHandler();

	private LoginHandler() {
	}

	public static LoginHandler getInstance() {
		return instance;
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		Map<String, String> form = request.body().bodyToMap();
		UserDatabase userDatabase = UserDatabase.getInstance();

		String userId = form.get("userId");
		String password = form.get("password");
		User user = userDatabase.get(userId);
		if (!(user.userId().equals(userId) && user.password().equals(password))) {
			throw new BaseException(HttpStatus.BAD_REQUEST, "Wrong id or password");
		}
		String uuid = UUID.randomUUID().toString();
		SessionDatabase.getInstance().insert(uuid, user.userId());
		setResponse(response);
		setCookie(response, uuid);
	}

	private void setResponse(HttpResponse response) {
		response.setStatusLine(HttpStatus.FOUND);
		HttpHeader header = new HttpHeader(new HashMap<>(Map.of("Location", "/index.html")));
		header.setDefaultHeaders();
		header.setHeaderValue("Content-Length", "0");
		response.setHeader(header);
	}

	private void setCookie(HttpResponse response, String uuid) {
		Cookie cookie = new Cookie("SID", uuid, Duration.ofMinutes(30), "/");
		response.addHeader("Set-Cookie", cookie.toString());
	}
}
