package codesquad.db;

public interface Database<T> {

	void insert(String id, T t);

	T get(String id);

	void update(String id, T t);

	void delete(String id);
}
