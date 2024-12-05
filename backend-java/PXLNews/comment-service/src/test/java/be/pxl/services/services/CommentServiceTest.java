package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.domain.dto.CommentUpdate;
import be.pxl.services.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    public void getAllCommentsReturnsCommentResponses() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setTitle("Test Title");
        comment.setContent("Test Content");
        comment.setAuthor("JohnDoe");
        comment.setPostId(1L);

        when(commentRepository.findByPostId(1L)).thenReturn(List.of(comment));

        // Act
        List<CommentResponse> comments = commentService.getAllComments(1L);

        // Assert
        assertEquals(1, comments.size());
        CommentResponse response = comments.get(0);
        assertEquals(1L, response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());
        assertEquals("JohnDoe", response.getAuthor());
        assertEquals(1L, response.getPostId());
    }

    @Test
    public void updateCommentUpdatesAndReturnsUpdatedComment() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setTitle("Old Title");
        comment.setContent("Old Content");

        CommentUpdate commentUpdate = new CommentUpdate();
        commentUpdate.setTitle("New Title");
        commentUpdate.setContent("New Content");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Act
        CommentResponse updatedComment = commentService.updateComment(1L, commentUpdate);

        // Assert
        verify(commentRepository).save(comment);
        assertEquals(1L, updatedComment.getId());
        assertEquals("New Title", updatedComment.getTitle());
        assertEquals("New Content", updatedComment.getContent());
    }

    @Test
    public void deleteCommentDeletesComment() {
        // Arrange
        Long commentId = 1L;

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    public void createCommentSavesAndReturnsCommentResponse() {
        // Arrange
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setTitle("Test Title");
        commentRequest.setContent("Test Content");
        commentRequest.setAuthor("JohnDoe");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setTitle("Test Title");
        comment.setContent("Test Content");
        comment.setAuthor("JohnDoe");
        comment.setPostId(1L);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentResponse response = commentService.createComment(1L, commentRequest);

        // Assert
        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentCaptor.capture());

        Comment savedComment = commentCaptor.getValue();
        assertEquals("Test Title", savedComment.getTitle());
        assertEquals("Test Content", savedComment.getContent());
        assertEquals("JohnDoe", savedComment.getAuthor());
        assertEquals(1L, savedComment.getPostId());

        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());
        assertEquals("JohnDoe", response.getAuthor());
        assertEquals(1L, response.getPostId());
    }

    @Test
    public void getCommentCountReturnsCorrectCount() {
        // Arrange
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        when(commentRepository.findByPostId(1L)).thenReturn(List.of(comment1, comment2));

        // Act
        Integer count = commentService.getCommentCount(1L);

        // Assert
        assertEquals(2, count);
    }

    @Test
    public void getCommentCountReturnsZeroWhenNoComments() {
        // Arrange
        when(commentRepository.findByPostId(1L)).thenReturn(List.of());

        // Act
        Integer count = commentService.getCommentCount(1L);

        // Assert
        assertEquals(0, count);
    }
}
