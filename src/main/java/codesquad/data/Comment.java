package codesquad.data;

import java.time.LocalDateTime;

public class Comment {
	private Long id;
	private String detail;
	private LocalDateTime createdAt;
	private Long parentId;
	private String userId;
	private Long postId;

	public Comment(Long id, String detail, LocalDateTime createdAt, Long parentId, String userId, Long postId) {
		this.id = id;
		this.detail = detail;
		this.createdAt = createdAt;
		this.parentId = parentId;
		this.userId = userId;
		this.postId = postId;
	}

	public Long getId() {
		return id;
	}

	public String getDetail() {
		return detail;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Long getParentId() {
		return parentId;
	}

	public String getUserId() {
		return userId;
	}

	public Long getPostId() {
		return postId;
	}
}
