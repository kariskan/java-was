package codesquad.handler;

import codesquad.db.SessionDatabase;
import codesquad.domain.Cookie;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;
import codesquad.utils.UserThreadLocal;
import java.time.Duration;

public class LogoutHandler extends DynamicHandler {

	private static final LogoutHandler instance = new LogoutHandler();

	private LogoutHandler() {
	}

	public static LogoutHandler getInstance() {
		return instance;
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		if (!UserThreadLocal.isLogin()) {
			throw new BaseException(HttpStatus.BAD_REQUEST, "You are not logged in");
		}
		Cookie sid = request.getCookie("SID");
		SessionDatabase.getInstance().delete(sid.getValue());
		removeCookie(response);
		response.sendRedirect("/index.html");
	}

	private void removeCookie(HttpResponse response) {
		Cookie cookie = new Cookie("SID", null, Duration.ofMillis(0), "/");
		response.addHeader("Set-Cookie", cookie.toString());
	}
}
