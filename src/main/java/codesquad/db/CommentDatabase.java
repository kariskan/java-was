package codesquad.db;

import java.sql.Timestamp;
import java.util.List;

import codesquad.data.Comment;
import codesquad.utils.CsvJdbcTemplate;

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
		return CsvJdbcTemplate.update(insert, ps -> {
			ps.setString(1, t.detail());
			ps.setTimestamp(2, Timestamp.valueOf(t.createdAt()));
			ps.setLong(3, t.parentId());
			ps.setString(4, t.userId());
		});
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
		return CsvJdbcTemplate.execute(sql, Comment.class, null);
	}
}
