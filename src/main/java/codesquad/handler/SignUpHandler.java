package codesquad.handler;

import codesquad.db.UserDatabase;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.HttpStatus;
import codesquad.domain.User;
import java.util.HashMap;
import java.util.Map;

public class SignUpHandler extends DynamicHandler {

	private static final SignUpHandler instance = new SignUpHandler();

	private SignUpHandler() {
	}

	public static SignUpHandler getInstance() {
		return instance;
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		User user = getUser(request);
		UserDatabase userDatabase = UserDatabase.getInstance();
		userDatabase.insert(user.userId(), user);

		setResponse(response);
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
}
