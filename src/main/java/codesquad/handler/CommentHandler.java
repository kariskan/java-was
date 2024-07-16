package codesquad.handler;

import codesquad.annotation.LoginCheck;
import codesquad.annotation.RequestMapping;
import codesquad.data.Comment;
import codesquad.db.CommentDatabase;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.utils.UserThreadLocal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class CommentHandler {

	public static final CommentDatabase commentDatabase = CommentDatabase.getInstance();

	@RequestMapping(httpMethod = HttpMethod.POST, url = "/comment")
	@LoginCheck
	public void addComment(HttpRequest request, HttpResponse response) {
		Map<String, String> form = request.body().bodyToMap();
		String detail = form.get("detail");
		String parentId = form.get("parentId");
		String postId = form.get("postId");
		String userId = UserThreadLocal.get().userId();
		Comment comment = new Comment(null, detail, LocalDateTime.now(),
			Objects.requireNonNullElse(Long.valueOf(parentId), null), userId,
			Objects.requireNonNullElse(Long.valueOf(postId), null));
		commentDatabase.insert(null, comment);
	}
}
