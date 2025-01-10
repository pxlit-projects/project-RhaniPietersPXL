package be.pxl.services.repository;

import be.pxl.services.ReviewServiceApplication;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.State;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ReviewServiceApplication.class)
@SpringBootTest
public class ReviewRepositoryTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Test
    public void testFindByPostId() {
        Post post = Post.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Jane")
                .creationDate(LocalDateTime.now())
                .category(Category.FOOD)
                .state(State.DRAFT)
                .build();
        post.setId(1L);

        // Mocking the PostRepository
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        Review review = new Review();
        review.setAuthor("Jane");
        review.setPostId(post.getId());
        review.setRejectedMessage("Test rejection message");

        // Mocking the ReviewRepository
        when(reviewRepository.findByPostId(post.getId())).thenReturn(Optional.of(review));

        Optional<Review> foundReview = reviewRepository.findByPostId(post.getId());

        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getPostId()).isEqualTo(post.getId());
        assertThat(foundReview.get().getAuthor()).isEqualTo("Jane");
    }

    @Test
    public void testFindByPostIdNotFound() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Review> review = reviewRepository.findByPostId(999L);
        assertThat(review).isNotPresent();
    }
}
