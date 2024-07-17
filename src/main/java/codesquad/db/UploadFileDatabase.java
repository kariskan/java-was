package codesquad.db;

import static codesquad.utils.Pair.of;
import static java.sql.JDBCType.BIGINT;
import static java.sql.JDBCType.BINARY;
import static java.sql.JDBCType.VARCHAR;

import codesquad.data.UploadFile;
import codesquad.utils.JdbcTemplate;
import java.util.List;

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
		return JdbcTemplate.update(insert, List.of(of(VARCHAR, t.filename()), of(BINARY, t.data())));
	}

	@Override
	public UploadFile get(Long id) {
		String find = """
			select *
			from UPLOADFILE
			where ID = ?
			""";
		return JdbcTemplate.executeOne(find, UploadFile.class, List.of(of(BIGINT, id)));
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
