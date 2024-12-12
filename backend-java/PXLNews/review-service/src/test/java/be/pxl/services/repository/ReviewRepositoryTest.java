package be.pxl.services.repository;

import be.pxl.services.ReviewServiceApplication;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = ReviewServiceApplication.class)
public class ReviewRepositoryTest {
    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
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
        entityManager.persist(post);
        entityManager.flush();

        Review review = new Review();
        review.setAuthor("Jane");
        review.setPostId(post.getId());
        review.setRejectedMessage("Test rejection message");
        entityManager.persist(review);
        entityManager.flush();

        Optional<Review> foundReview = reviewRepository.findByPostId(post.getId());

        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getPostId()).isEqualTo(post.getId());
        assertThat(foundReview.get().getAuthor()).isEqualTo("Jane");
    }

    @Test
    public void testFindByPostIdNotFound() {
        Optional<Review> review = reviewRepository.findByPostId(999L);
        assertThat(review).isNotPresent();
    }
}
