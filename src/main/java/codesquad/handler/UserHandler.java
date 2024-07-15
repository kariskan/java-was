package codesquad.handler;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import codesquad.annotation.LoginCheck;
import codesquad.annotation.RequestMapping;
import codesquad.db.SessionDatabase;
import codesquad.db.UserDatabase;
import codesquad.domain.Cookie;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.HttpStatus;
import codesquad.data.User;
import codesquad.error.BaseException;
import codesquad.utils.UserThreadLocal;

public class UserHandler {

	private UserHandler() {
	}

	@RequestMapping(httpMethod = HttpMethod.POST, url = "/login")
	public void login(HttpRequest request, HttpResponse response) {
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

	@RequestMapping(httpMethod = HttpMethod.POST, url = "/logout")
	@LoginCheck
	public void logout(HttpRequest request, HttpResponse response) {
		if (!UserThreadLocal.isLogin()) {
			throw new BaseException(HttpStatus.BAD_REQUEST, "You are not logged in");
		}
		Cookie sid = request.getCookie("SID");
		SessionDatabase.getInstance().delete(sid.getValue());
		removeCookie(response);
		response.sendRedirect("/index.html");
	}

	@RequestMapping(httpMethod = HttpMethod.POST, url = "/create")
	public void signup(HttpRequest request, HttpResponse response) {
		User user = getUser(request);
		UserDatabase userDatabase = UserDatabase.getInstance();
		userDatabase.insert(user.userId(), user);

		setResponse(response);
	}

	@RequestMapping(httpMethod = HttpMethod.GET, url = "/user/list")
	@LoginCheck
	public void userList(HttpRequest request, HttpResponse response) {
		response.sendRedirect("/user/list.html");
	}

	private void setResponse(HttpResponse response) {
		response.setStatusLine(HttpStatus.FOUND);
		HttpHeader header = new HttpHeader(new HashMap<>(Map.of("Location", "/index.html")));
		header.setDefaultHeaders();
		header.setHeaderValue("Content-Length", "0");
		response.setHeader(header);
	}

	private User getUser(HttpRequest request) {
		Map<String, String> params = request.body().bodyToMap();
		String userId = params.get("userId");
		String password = params.get("password");
		String email = params.get("email");
		String nickname = params.get("nickname");

		return new User(userId, nickname, password, email);
	}

	private void removeCookie(HttpResponse response) {
		Cookie cookie = new Cookie("SID", null, Duration.ofMillis(0), "/");
		response.addHeader("Set-Cookie", cookie.toString());
	}

	private void setCookie(HttpResponse response, String uuid) {
		Cookie cookie = new Cookie("SID", uuid, Duration.ofMinutes(30), "/");
		response.addHeader("Set-Cookie", cookie.toString());
	}
}
