package codesquad.db;

import java.util.List;

import codesquad.data.Post;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;
import codesquad.utils.CsvJdbcTemplate;
import codesquad.utils.CsvUtil;

public class PostDatabase implements Database<Long, Post> {

	private static final PostDatabase INSTANCE = new PostDatabase();

	private PostDatabase() {
	}

	public static PostDatabase getInstance() {
		return INSTANCE;
	}

	@Override
	public Long insert(Long id, Post t) {
		String insert = """
			insert into POST (id, title, content, userId, uploadFileId)
			values (?, ?, ?, ?, ?)
			""";
		id = (long)CsvUtil.readCsv("POST").size();
		Long finalId = id;
		CsvJdbcTemplate.update(insert, ps -> {
			ps.setLong(1, finalId);
			ps.setString(2, t.title());
			ps.setString(3, t.content());
			ps.setString(4, t.userId());
			ps.setLong(5, t.uploadFileId());
		});
		return finalId;
	}

	@Override
	public Post get(Long id) {
		String find = """
			select *
			from post
			where id = ?
			""";
		Post post = CsvJdbcTemplate.executeOne(find, Post.class, ps -> ps.setLong(1, id));
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
		return CsvJdbcTemplate.execute(find, Post.class, null);
	}
}
