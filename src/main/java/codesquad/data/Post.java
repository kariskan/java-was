package codesquad.data;

public record Post(
	Long id,
	String title,
	String content,
	String userId
) {
}
