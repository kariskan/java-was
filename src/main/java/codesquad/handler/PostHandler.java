package codesquad.handler;

import codesquad.annotation.LoginCheck;
import codesquad.annotation.RequestMapping;
import codesquad.data.Post;
import codesquad.data.User;
import codesquad.db.PostDatabase;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.utils.UserThreadLocal;
import java.util.Map;

public class PostHandler {

	private static final PostDatabase postDatabase = PostDatabase.getInstance();

	private PostHandler() {
	}

	@RequestMapping(httpMethod = HttpMethod.GET, url = "/write")
	@LoginCheck
	public void redirectPost(HttpRequest request, HttpResponse response) {
		response.sendRedirect("/write.html");
	}

	@RequestMapping(httpMethod = HttpMethod.POST, url = "/post")
	@LoginCheck
	public void addPost(HttpRequest request, HttpResponse response) {
		User user = UserThreadLocal.get();
		Map<String, String> form = request.body().bodyToMap();
		String title = form.get("title");
		String content = form.get("content");

		postDatabase.insert(null, new Post(null, title, content, user.userId()));
		response.sendRedirect("/index.html");
	}
}
