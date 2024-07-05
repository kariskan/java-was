package codesquad.handler;

import codesquad.db.UserDatabase;
import codesquad.domain.HttpHeader;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.domain.HttpStatus;
import codesquad.domain.Parameters;
import codesquad.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUpHandler extends DynamicHandler {

	private static final SignUpHandler instance = new SignUpHandler();

	private SignUpHandler() {
	}

	public static SignUpHandler getInstance() {
		return instance;
	}

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		User user = getUser(request);
		UserDatabase userDatabase = UserDatabase.getInstance();
		userDatabase.insert(UUID.randomUUID().toString(), user);

		setResponse(response);
	}

	private void setResponse(HttpResponse response) {
		response.setStatusLine(HttpStatus.MOVED_PERMANENTLY);
		HttpHeader header = new HttpHeader(new HashMap<>(Map.of("Location", "/index.html")));
		header.setDefaultHeaders();
		header.setHeaderValue("Content-Length", "0");
		response.setHeader(header);
	}

	private User getUser(HttpRequest request) {
		Parameters parameters = request.getParameters();
		String userId = parameters.getValueByKey("userId");
		String password = parameters.getValueByKey("password");
		String email = parameters.getValueByKey("email");
		String nickname = parameters.getValueByKey("nickname");

		return new User(userId, nickname, password, email);
	}
}
