package be.pxl.services.repository;

import be.pxl.services.PostServiceApplication;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = PostServiceApplication.class)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.7");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        postRepository.deleteAll();
        Post post1 = Post.builder().title("Post 1").content("Content of post 1").author("Author 1").creationDate(LocalDateTime.now()).category(Category.FOOD).state(State.DRAFT).build();
        postRepository.save(post1);

        Post post2 = Post.builder().title("Post 2").content("Content of post 2").creationDate(LocalDateTime.now()).category(Category.TEACHERS).state(State.PENDING_APPROVAL).build();
        postRepository.save(post2);

        Post post3 = Post.builder().title("Post 3").content("Content of post 3").author("Author 2").creationDate(LocalDateTime.now()).category(Category.STUDENTS).state(State.APPROVED).build();
        postRepository.save(post3);

        Post post4 = Post.builder().title("Post 4").content("Content of post 4").author("Author 2").creationDate(LocalDateTime.now()).category(Category.EVENTS).state(State.REJECTED).rejectedMessage("Rejected due to issues").build();
        postRepository.save(post4);
    }

    @Test
    public void testFindByState() {
        List<Post> draftPosts = postRepository.findByState(State.DRAFT);
        assertThat(draftPosts).hasSize(1);
        assertThat(draftPosts.get(0).getState()).isEqualTo(State.DRAFT);
    }

    @Test
    public void testFindByStateNoResults() {
        List<Post> publishedPosts = postRepository.findByState(State.PUBLISHED);
        assertThat(publishedPosts).isEmpty();
    }

    @Test
    public void testFindByAuthorAndStateNotIn() {
        List<State> excludedStates = Arrays.asList(State.PENDING_APPROVAL, State.REJECTED);
        List<Post> posts = postRepository.findByAuthorAndStateNotIn("Author 1", excludedStates);

        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getAuthor()).isEqualTo("Author 1");
        assertThat(posts.get(0).getState()).isNotIn(State.PENDING_APPROVAL, State.REJECTED);
    }

    @Test
    public void testSaveAndRetrievePost() {
        Post newPost = Post.builder().title("New Post").content("New Content").author("New Author").creationDate(LocalDateTime.now()).category(Category.ADMINISTRATIVE).state(State.PENDING_APPROVAL).build();
        Post savedPost = postRepository.save(newPost);
        Post retrievedPost = postRepository.findById(savedPost.getId()).orElse(null);

        assertThat(retrievedPost).isNotNull();
        assertThat(retrievedPost.getTitle()).isEqualTo("New Post");
        assertThat(retrievedPost.getContent()).isEqualTo("New Content");
        assertThat(retrievedPost.getAuthor()).isEqualTo("New Author");
        assertThat(retrievedPost.getState()).isEqualTo(State.PENDING_APPROVAL);
    }
}
