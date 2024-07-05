package codesquad.domain;

public record User(
	String userId,
	String nickname,
	String password,
	String email
) {
}
