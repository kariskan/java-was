package codesquad.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface QuerySetter {

	void setValues(PreparedStatement ps) throws SQLException;
}
