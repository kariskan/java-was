package codesquad.handler;

import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import codesquad.data.Comment;
import codesquad.data.User;
import codesquad.db.CommentDatabase;
import codesquad.domain.HttpBody;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.utils.UserThreadLocal;

@DisplayName("CommentHandler 클래스 테스트")
class CommentHandlerTest {

	@Mock
	HttpRequest request;

	@Mock
	HttpResponse response;

	@Mock
	CommentDatabase commentDatabase;

	CommentHandler commentHandler;

	@BeforeEach
	void setUp() throws ReflectiveOperationException {
		MockitoAnnotations.openMocks(this);
		UserThreadLocal.set(new User("user1", "nickname", "password", "email"));

		Constructor<CommentHandler> constructor = CommentHandler.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		commentHandler = constructor.newInstance();
	}

	@Nested
	@DisplayName("addComment 메서드")
	class AddCommentTest {

		@Test
		@DisplayName("댓글 추가 테스트")
		void addComment_shouldAddComment() {
			when(request.body()).thenReturn(new HttpBody("detail=comment detail&parentId=1&postId=1".getBytes()));

			commentHandler.addComment(request, response);

			ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
			verifyNoInteractions(commentDatabase);
		}
	}
}
