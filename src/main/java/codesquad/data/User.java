package codesquad.data;

public record User(
	String userId,
	String nickname,
	String password,
	String email
) {
}
