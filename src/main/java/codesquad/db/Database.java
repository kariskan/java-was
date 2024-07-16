package codesquad.db;

import java.util.List;

public interface Database<T, E> {

	void insert(T id, E t);

	E get(T id);

	void update(T id, E t);

	void delete(T id);

	List<E> findAll();
}
