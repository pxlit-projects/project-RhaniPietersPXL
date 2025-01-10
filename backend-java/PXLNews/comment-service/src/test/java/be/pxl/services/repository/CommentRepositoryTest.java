package be.pxl.services.repository;

import be.pxl.services.CommentServiceApplication;
import be.pxl.services.domain.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = CommentServiceApplication.class)
public class CommentRepositoryTest {

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        commentRepository.deleteAll();
        Comment comment1 = new Comment();
        comment1.setTitle("First Comment");
        comment1.setContent("Content of first comment");
        comment1.setAuthor("Author 1");
        comment1.setPostId(1L);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setTitle("Second Comment");
        comment2.setContent("Content of second comment");
        comment2.setAuthor("Author 2");
        comment2.setPostId(1L);
        commentRepository.save(comment2);

        Comment comment3 = new Comment();
        comment3.setTitle("Third Comment");
        comment3.setContent("Content of third comment");
        comment3.setAuthor("Author 3");
        comment3.setPostId(2L);
        commentRepository.save(comment3);
        when(commentRepository.findByPostId(1L)).thenReturn(Arrays.asList(comment1, comment2));

    }

    @Test
    public void testFindByPostId() {

        List<Comment> comments = commentRepository.findByPostId(1L);

        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getPostId()).isEqualTo(1L);
        assertThat(comments.get(1).getPostId()).isEqualTo(1L);
    }

    @Test
    public void testFindByPostIdNoResults() {
        List<Comment> comments = commentRepository.findByPostId(999L);
        assertThat(comments).isEmpty();
    }

    @Test
    public void testFindAllCommentsForPost() {
        List<Comment> comments = commentRepository.findByPostId(1L);
        assertThat(comments).isNotEmpty();
        assertThat(comments.size()).isEqualTo(2);
    }

    @Test
    public void testSaveAndRetrieveComment() {
        Comment newComment = new Comment();
        newComment.setTitle("New Comment");
        newComment.setContent("New Content");
        newComment.setAuthor("New Author");
        newComment.setPostId(1L);

        when(commentRepository.save(newComment)).thenReturn(newComment);

        newComment.setId(10L);
        when(commentRepository.findById(10L)).thenReturn(java.util.Optional.of(newComment));

        Comment savedComment = commentRepository.save(newComment);
        Comment retrievedComment = commentRepository.findById(savedComment.getId()).orElse(null);

        assertThat(retrievedComment).isNotNull();
        assertThat(retrievedComment.getTitle()).isEqualTo("New Comment");
        assertThat(retrievedComment.getContent()).isEqualTo("New Content");
        assertThat(retrievedComment.getAuthor()).isEqualTo("New Author");
        assertThat(retrievedComment.getPostId()).isEqualTo(1L);
    }

    @Test
    public void testDeleteComment() {
        // Mock bestaande comments
        Comment comment1 = new Comment();
        comment1.setId(1L);
        Comment comment2 = new Comment();
        comment2.setId(2L);

        when(commentRepository.findByPostId(1L)).thenReturn(Arrays.asList(comment1, comment2));

        when(commentRepository.findByPostId(1L)).thenReturn(Arrays.asList(comment2));

        commentRepository.deleteById(1L);

        assertThat(commentRepository.findByPostId(1L)).hasSize(1);
        assertThat(commentRepository.findByPostId(1L).get(0).getId()).isEqualTo(2L);
    }
}
