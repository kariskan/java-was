package codesquad.db;

import static codesquad.utils.Pair.of;
import static java.sql.JDBCType.BIGINT;
import static java.sql.JDBCType.TIMESTAMP;
import static java.sql.JDBCType.VARCHAR;

import codesquad.data.Comment;
import codesquad.utils.JdbcTemplate;
import codesquad.utils.Pair;
import java.sql.SQLType;
import java.util.List;

public class CommentDatabase implements Database<Long, Comment> {

	private static final CommentDatabase INSTANCE = new CommentDatabase();

	private CommentDatabase() {
	}

	public static CommentDatabase getInstance() {
		return INSTANCE;
	}

	@Override
	public Long insert(Long id, Comment t) {
		String insert = """
			INSERT INTO comment (detail, createdAt, parentId, userId)
			VALUES (?, ?, ?, ?);
			""";
		List<Pair<SQLType, Object>> list = List.of(of(VARCHAR, t.detail()), of(TIMESTAMP, t.createdAt()),
			of(BIGINT, t.parentId()), of(VARCHAR, t.userId()));
		return JdbcTemplate.update(insert, list);
	}

	@Override
	public Comment get(Long id) {
		return null;
	}

	@Override
	public void update(Long id, Comment t) {

	}

	@Override
	public void delete(Long id) {

	}

	@Override
	public List<Comment> findAll() {
		String sql = """
			select * from comment
			""";
		return JdbcTemplate.execute(sql, Comment.class, null);
	}
}
