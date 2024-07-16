package codesquad.db;

import static codesquad.utils.Pair.of;

import codesquad.data.Post;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;
import codesquad.utils.JdbcTemplate;
import codesquad.utils.Pair;
import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.List;

public class PostDatabase implements Database<Long, Post> {

	private static final PostDatabase INSTANCE = new PostDatabase();

	private PostDatabase() {
	}

	public static PostDatabase getInstance() {
		return INSTANCE;
	}

	@Override
	public void insert(Long id, Post t) {
		String insert = """
			insert into POST(title, content, userId)
			values (?, ?, ?)
			""";
		List<Pair<SQLType, Object>> list = List.of(of(JDBCType.VARCHAR, t.title()),
			of(JDBCType.VARCHAR, t.content()), of(JDBCType.VARCHAR, t.userId()));
		JdbcTemplate.update(insert, list);
	}

	@Override
	public Post get(Long id) {
		String find = """
			select *
			from post
			where id = ?
			""";
		Post post = JdbcTemplate.executeOne(find, Post.class, List.of(of(JDBCType.BIGINT, id)));
		if (post == null) {
			throw new BaseException(HttpStatus.NOT_FOUND, "post not found");
		}
		return post;
	}

	@Override
	public void update(Long id, Post t) {

	}

	@Override
	public void delete(Long id) {

	}

	@Override
	public List<Post> findAll() {
		String find = """
			select *
			from post
			""";
		return JdbcTemplate.execute(find, Post.class, null);
	}
}
