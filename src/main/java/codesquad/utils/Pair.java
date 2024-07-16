package codesquad.utils;

public class Pair<L, R> {

	private L left;
	private R right;

	public static <L, R> Pair<L, R> of(L left, R right) {
		var result = new Pair<L, R>();
		result.left = left;
		result.right = right;
		return result;
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}
}
