package codesquad.db;

import codesquad.utils.CsvJdbcTemplate;
import codesquad.utils.CsvUtil;
import java.util.List;

import codesquad.data.UploadFile;
import codesquad.utils.DbJdbcTemplate;

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
			insert into UPLOADFILE (ID, FILENAME, DATA)
			values (?, ?, ?)
			""";
		id = (long) CsvUtil.readCsv("UPLOADFILE").size();
		Long finalId = id;
		CsvJdbcTemplate.update(insert, ps -> {
			ps.setLong(1, finalId);
			ps.setString(2, t.filename());
			ps.setBytes(3, t.data());
		});
		return finalId;
	}

	@Override
	public UploadFile get(Long id) {
		String find = """
			select *
			from UPLOADFILE
			where ID = ?
			""";
		return CsvJdbcTemplate.executeOne(find, UploadFile.class, ps -> ps.setLong(1, id));
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
