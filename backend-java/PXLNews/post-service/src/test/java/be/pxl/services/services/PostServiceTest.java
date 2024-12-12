package be.pxl.services.services;

import be.pxl.services.client.CommentClient;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.*;
import be.pxl.services.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentClient commentClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void testEditPost() {
        // Arrange
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setTitle("Original Title");
        post.setContent("Original Content");
        post.setAuthor("Author");
        post.setCategory(Category.TEACHERS);
        post.setState(State.PUBLISHED);

        PostUpdateRequest updateRequest = new PostUpdateRequest("Updated Title", "Updated Content", Category.STUDENTS);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        // Act
        PostResponse updatedPost = postService.editPost(updateRequest, postId);

        // Assert
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Updated Content", updatedPost.getContent());
        assertEquals(Category.STUDENTS, updatedPost.getCategory());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testGetAllPublishedPosts() {
        // Arrange
        Post post1 = new Post();
        post1.setId(1L);
        post1.setState(State.PUBLISHED);
        post1.setCategory(Category.FOOD);
        post1.setTitle("Published Post 1");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setState(State.PUBLISHED);
        post2.setCategory(Category.EVENTS);
        post2.setTitle("Published Post 2");

        List<Post> posts = List.of(post1, post2);

        when(postRepository.findByState(State.PUBLISHED)).thenReturn(posts);
        when(commentClient.getCommentCountForPost(1L)).thenReturn(10);
        when(commentClient.getCommentCountForPost(2L)).thenReturn(5);

        // Act
        List<PostResponse> postResponses = postService.getAllPublishedPosts();

        // Assert
        assertEquals(2, postResponses.size());
        assertEquals(10, postResponses.get(0).getCommentCount());
        assertEquals(5, postResponses.get(1).getCommentCount());
    }

    @Test
    void testGetPostById() {
        // Arrange
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setTitle("Test Title");
        post.setContent("Test Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        PostResponse postResponse = postService.getPostById(postId);

        // Assert
        assertNotNull(postResponse);
        assertEquals("Test Title", postResponse.getTitle());
    }

    @Test
    void testDeletePostById() {
        // Arrange
        Long postId = 1L;

        // Act
        postService.deletePostById(postId);

        // Assert
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void testGetDraftsFromAuthor() {
        // Arrange
        String author = "Test Author";
        Post draftPost = new Post();
        draftPost.setId(1L);
        draftPost.setTitle("Draft Post");
        draftPost.setState(State.DRAFT);
        draftPost.setAuthor(author);

        when(postRepository.findByAuthorAndStateNotIn(author, List.of(State.PUBLISHED))).thenReturn(List.of(draftPost));

        // Act
        List<PostResponse> drafts = postService.getDraftsFromAuthor(author);

        // Assert
        assertEquals(1, drafts.size());
        assertEquals("Draft Post", drafts.get(0).getTitle());
    }

    @Test
    void testGetApproval() {
        // Arrange
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setState(State.PENDING_APPROVAL);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        PostResponse postResponse = postService.getApproval(postId);

        // Assert
        assertEquals(State.PENDING_APPROVAL, postResponse.getState());
        verify(rabbitTemplate, times(1)).convertAndSend(eq("getApproval"), any(ReviewRequestMessage.class));
    }

    @Test
    void testSetReviewStatus() {
        // Arrange
        Long postId = 1L;
        ReviewApprovalMessage approvalMessage = new ReviewApprovalMessage(postId, "APPROVED", "");
        Post post = new Post();
        post.setId(postId);
        post.setState(State.PENDING_APPROVAL);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        postService.setReviewStatus(approvalMessage);

        // Assert
        assertEquals(State.APPROVED, post.getState());
    }

    @Test
    void testPublishPost() {
        // Arrange
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setState(State.PENDING_APPROVAL);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        postService.publishPost(postId);

        // Assert
        assertEquals(State.PUBLISHED, post.getState());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testAddNewPost() {
        // Arrange
        PostCreateRequest newPostRequest = new PostCreateRequest("New Post", "Content", "Author", LocalDateTime.now(), Category.FOOD, State.DRAFT);
        Post savedPost = new Post();
        savedPost.setId(1L);
        savedPost.setTitle("New Post");
        savedPost.setContent("Content");
        savedPost.setAuthor("Author");
        savedPost.setCategory(Category.FOOD);
        savedPost.setState(State.DRAFT);

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // Act
        PostResponse createdPostResponse = postService.addNewPost(newPostRequest);

        // Assert
        assertEquals("New Post", createdPostResponse.getTitle());
        assertEquals("Content", createdPostResponse.getContent());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testEditPostThrowsExceptionWhenPostNotFound() {
        // Arrange
        Long postId = 1L;
        PostUpdateRequest updateRequest = new PostUpdateRequest("Updated Title", "Updated Content", Category.STUDENTS);

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            postService.editPost(updateRequest, postId);
        });
    }

    @Test
    void testGetPostByIdThrowsExceptionWhenPostNotFound() {
        // Arrange
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            postService.getPostById(postId);
        });
    }

    @Test
    void testGetDraftsFromAuthorReturnsEmptyListWhenNoDrafts() {
        // Arrange
        String author = "Test Author";

        when(postRepository.findByAuthorAndStateNotIn(author, List.of(State.PUBLISHED))).thenReturn(List.of());

        // Act
        List<PostResponse> drafts = postService.getDraftsFromAuthor(author);

        // Assert
        assertTrue(drafts.isEmpty());
    }

    @Test
    void testSetReviewStatusThrowsExceptionWhenPostNotFound() {
        // Arrange
        ReviewApprovalMessage approvalMessage = new ReviewApprovalMessage(1L, "APPROVED", "");

        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            postService.setReviewStatus(approvalMessage);
        });
    }

    @Test
    void testPublishPostThrowsExceptionWhenPostNotFound() {
        // Arrange
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            postService.publishPost(postId);
        });
    }

    @Test
    void testGetApprovalThrowsExceptionWhenPostNotFound() {
        // Arrange
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            postService.getApproval(postId);
        });
    }
}
