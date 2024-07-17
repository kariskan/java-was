package codesquad.handler;

import codesquad.annotation.LoginCheck;
import codesquad.annotation.RequestMapping;
import codesquad.data.Post;
import codesquad.data.UploadFile;
import codesquad.data.User;
import codesquad.db.PostDatabase;
import codesquad.db.UploadFileDatabase;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.utils.UserThreadLocal;
import java.util.Map;

public class PostHandler {

	private static final PostDatabase postDatabase = PostDatabase.getInstance();
	private static final UploadFileDatabase uploadFileDatabase = UploadFileDatabase.getInstance();

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
		Map<String, Object> form = request.body().bodyToMultipart(getBoundary(request.header().getHeaderValue("Content-Type")));
		Object title = form.get("title");
		Object content = form.get("content");
		UploadFile image = (UploadFile) form.get("image");

		Long uploadFileId = uploadFileDatabase.insert(null, image);
		postDatabase.insert(null, new Post(null, String.valueOf(title), String.valueOf(content), user.userId(), uploadFileId));

		response.sendRedirect("/index.html");

	}

	private String getBoundary(String contentType) {
		String[] params = contentType.split(";");
		for (String param : params) {
			param = param.trim();
			if (param.startsWith("boundary=")) {
				return param.substring("boundary=".length());
			}
		}
		return null;
	}
}
