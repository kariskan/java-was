package codesquad.data;

import java.time.LocalDateTime;

public record Comment(
	Long id,
	String detail,
	LocalDateTime createdAt,
	Long parentId,
	String userId,
	Long postId
) {
}
