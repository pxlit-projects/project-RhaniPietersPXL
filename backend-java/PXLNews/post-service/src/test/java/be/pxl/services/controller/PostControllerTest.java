package be.pxl.services.controller;

import be.pxl.services.PostServiceApplication;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.dto.PostCreateRequest;

import static org.junit.jupiter.api.Assertions.*;

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

        mockMvc.perform(get("/post")
                .header("role", "redacteur")
        ).andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();
        assertFalse(posts.isEmpty(), "Posts list should not be empty");
    }

    @Test
    public void verifyCreatePostReturnsCreated() throws Exception {
        PostCreateRequest postCreateRequest = new PostCreateRequest("New Post", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.DRAFT);

        String json = objectMapper.writeValueAsString(postCreateRequest);

        mockMvc.perform(post("/post")
                        .header("role", "redacteur")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        assertEquals(1, postRepository.findAll().size(), "Post should be created successfully");
    }

    @Test
    public void verifyEditPostReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.DRAFT, null);
        post = postRepository.save(post);

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("Updated Post", "Updated Content", Category.FOOD);

        String json = objectMapper.writeValueAsString(postUpdateRequest);
        mockMvc.perform(put("/post/{id}", post.getId())
                        .header("role", "redacteur")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        Optional<Post> updatedPost = postRepository.findById(post.getId());
        assertAll("post assertions",
                () -> assertTrue(updatedPost.isPresent(), "Post should be updated"),
                () -> assertEquals("Updated Post", updatedPost.get().getTitle(), "Title should be updated"),
                () -> assertEquals("Updated Content", updatedPost.get().getContent(), "Content should be updated")
        );
    }

    @Test
    public void verifyPublishPostReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.DRAFT, null);
        post = postRepository.save(post);

        mockMvc.perform(post("/post/{id}/publish", post.getId())
                        .header("role", "redacteur")
                )
                .andExpect(status().isOk());

        Optional<Post> publishedPost = postRepository.findById(post.getId());
        assertAll("published post assertions",
                () -> assertTrue(publishedPost.isPresent(), "Post should be published"),
                () -> assertEquals(State.PUBLISHED, publishedPost.get().getState(), "Post state should be PUBLISHED")
        );
    }

    @Test
    public void verifyGetPostByIdReturnsOk() throws Exception {
        Post post = new Post(null, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null);
        post = postRepository.save(post);
        mockMvc.perform(get("/post/{id}", post.getId())
                        .header("role", "redacteur")
                )
                .andExpect(status().isOk());

        Optional<Post> fetchedPost = postRepository.findById(post.getId());
        assertTrue(fetchedPost.isPresent(), "Post should be found");
        assertEquals("Post Title", fetchedPost.get().getTitle(), "Post title should match");
    }

    @Test
    public void verifyGetDraftsFromAuthorReturnsOk() throws Exception {
        String author = "Author 1";

        Post draft1 = new Post(null, "Draft Title 1", "Draft Content 1", author, LocalDateTime.now(), Category.FOOD, State.DRAFT, null);
        Post draft2 = new Post(null, "Draft Title 2", "Draft Content 2", author, LocalDateTime.now(), Category.FOOD, State.DRAFT, null);

        postRepository.save(draft1);
        postRepository.save(draft2);

        mockMvc.perform(get("/post/drafts/{author}", author)
                        .header("user", "Author 1"))
                .andExpect(status().isOk());

        List<Post> drafts = postRepository.findByAuthorAndStateNotIn(author, List.of(State.PUBLISHED));
        assertEquals(2, drafts.size(), "There should be 2 drafts");
        assertAll("drafts assertions",
                () -> assertEquals(State.DRAFT, drafts.get(0).getState(), "Draft 1 should have state DRAFT"),
                () -> assertEquals(State.DRAFT, drafts.get(1).getState(), "Draft 2 should have state DRAFT")
        );
    }
}
