package be.pxl.services.controller;

import be.pxl.services.PostServiceApplication;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.dto.PostCreateRequest;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.PostUpdateRequest;
import be.pxl.services.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = PostServiceApplication.class)
@SpringBootTest()
@Testcontainers
@AutoConfigureMockMvc
public class PostControllerTest {

    //Container + services aanzetten voor de testen
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void clearRepository() {
        postRepository.deleteAll();
    }

    @Test
    public void verifyGetAllPostsReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title 1", "Post Content 1", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null);
        postRepository.save(post);

        mockMvc.perform(get("/post")).andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();
        assert (posts.size() > 0);
    }


    @Test
    public void verifyCreatePostReturnsCreated() throws Exception {
        PostCreateRequest postCreateRequest = new PostCreateRequest("New Post", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.DRAFT);

        String json = objectMapper.writeValueAsString(postCreateRequest);

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        assertEquals(1, postRepository.findAll().size());
    }

    @Test
    public void verifyDeletePostReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null);
        post = postRepository.save(post);

        mockMvc.perform(delete("/post/{id}", post.getId()))
                .andExpect(status().isOk());

        Optional<Post> deletedPost = postRepository.findById(post.getId());
        assert (deletedPost.isEmpty());
    }

    @Test
    public void verifyGetApprovalReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null);
        post = postRepository.save(post);

        Long postId = post.getId();

        mockMvc.perform(post("/post/{id}/approval", postId))
                .andExpect(status().isOk());
    }

    @Test
    public void verifyEditPostReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.DRAFT, null);
        post = postRepository.save(post);

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("Updated Post", "Updated Content", Category.FOOD);

        String json = objectMapper.writeValueAsString(postUpdateRequest);
        mockMvc.perform(put("/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        Optional<Post> updatedPost = postRepository.findById(post.getId());
        assert (updatedPost.isPresent());
        assert (updatedPost.get().getTitle().equals("Updated Post"));
        assert (updatedPost.get().getContent().equals("Updated Content"));
    }

    @Test
    public void verifyPublishPostReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.DRAFT, null);
        post = postRepository.save(post);

        mockMvc.perform(post("/post/{id}/publish", post.getId()))
                .andExpect(status().isOk());

        Optional<Post> publishedPost = postRepository.findById(post.getId());
        assert (publishedPost.isPresent());
        assert (publishedPost.get().getState().equals(State.PUBLISHED));
    }

    @Test
    public void verifyGetPostByIdReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null);
        post = postRepository.save(post);
        mockMvc.perform(get("/post/{id}", post.getId()))
                .andExpect(status().isOk());

        Optional<Post> fetchedPost = postRepository.findById(post.getId());
        assert (fetchedPost.isPresent());
        assert (fetchedPost.get().getTitle().equals("Post Title"));
    }

    @Test
    public void verifyGetDraftsFromAuthorReturnsOk() throws Exception {
        String author = "Author 1";

        Post draft1 = new Post(null, "Draft Title 1", "Draft Content 1", author, LocalDateTime.now(), Category.FOOD, State.DRAFT, null);
        Post draft2 = new Post(null, "Draft Title 2", "Draft Content 2", author, LocalDateTime.now(), Category.FOOD, State.DRAFT, null);

        postRepository.save(draft1);
        postRepository.save(draft2);

        mockMvc.perform(get("/post/drafts/{author}", author))
                .andExpect(status().isOk());

        List<Post> drafts = postRepository.findByAuthorAndStateNotIn(author, List.of(State.PUBLISHED));
        assert (drafts.size() == 2);
        assert (drafts.get(0).getState().equals(State.DRAFT));
        assert (drafts.get(1).getState().equals(State.DRAFT));
    }
}
