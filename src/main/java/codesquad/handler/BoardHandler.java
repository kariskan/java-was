package codesquad.handler;

import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.List;
import java.util.Map;

import codesquad.annotation.LoginCheck;
import codesquad.annotation.RequestMapping;
import codesquad.data.Post;
import codesquad.domain.HttpMethod;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.utils.JdbcTemplate;

public class BoardHandler {

	private BoardHandler() {
	}

	@RequestMapping(httpMethod = HttpMethod.GET, url = "/write")
	@LoginCheck
	public void redirectBoard(HttpRequest request, HttpResponse response) {
		response.sendRedirect("/write.html");
	}

	@RequestMapping(httpMethod = HttpMethod.POST, url = "/board")
	@LoginCheck
	public void addPost(HttpRequest request, HttpResponse response) {
		String insert = """
			insert into POST(title, content)
			values (?, ?)
			""";
		Map<String, String> form = request.body().bodyToMap();
		String title = form.get("title");
		String content = form.get("content");

		List<Map.Entry<SQLType, Object>> list = List.of(Map.entry(JDBCType.VARCHAR, title),
			Map.entry(JDBCType.VARCHAR, content));
		JdbcTemplate.update(insert, list);
		response.sendRedirect("/write");
	}

	@RequestMapping(httpMethod = HttpMethod.GET, url = "/board")
	public void getPost(HttpRequest request, HttpResponse response) {
		String select = """
			select * from POST
			""";
		List<Post> execute = JdbcTemplate.execute(select, Post.class, null);
	}
}
