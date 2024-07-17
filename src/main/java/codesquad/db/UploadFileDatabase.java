package codesquad.db;

import java.util.List;

import codesquad.data.UploadFile;
import codesquad.utils.JdbcTemplate;

public class UploadFileDatabase implements Database<Long, UploadFile> {

	public static final UploadFileDatabase INSTANCE = new UploadFileDatabase();

	private UploadFileDatabase() {
	}

	public static UploadFileDatabase getInstance() {
		return INSTANCE;
	}

	@Override
	public Long insert(Long id, UploadFile t) {
		String insert = """
			insert into UPLOADFILE (FILENAME, DATA)
			values (?, ?)
			""";
		return JdbcTemplate.update(insert, ps -> {
			ps.setString(1, t.filename());
			ps.setBytes(2, t.data());
		});
	}

	@Override
	public UploadFile get(Long id) {
		String find = """
			select *
			from UPLOADFILE
			where ID = ?
			""";
		return JdbcTemplate.executeOne(find, UploadFile.class, ps -> ps.setLong(1, id));
	}

	@Override
	public void update(Long id, UploadFile t) {

	}

	@Override
	public void delete(Long id) {

	}

	@Override
	public List<UploadFile> findAll() {
		return List.of();
	}
}
