package be.pxl.services.repository;

import be.pxl.services.domain.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class ReviewRepositoryTest {
    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void testFindByPostId() {
        Optional<Review> review = reviewRepository.findByPostId(2L);

        assertThat(review).isPresent();
        assertThat(review.get().getPostId()).isEqualTo(2L);
        assertThat(review.get().getAuthor()).isEqualTo("Jane");
    }

    @Test
    public void testFindByPostIdNotFound() {
        Optional<Review> review = reviewRepository.findByPostId(999L);
        assertThat(review).isNotPresent();
    }
}
