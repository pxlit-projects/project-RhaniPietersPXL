package be.pxl.services.controller;

import be.pxl.services.PostServiceApplication;
import be.pxl.services.domain.dto.PostCreateRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.PostUpdateRequest;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.State;
import be.pxl.services.services.IPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@ContextConfiguration(classes = PostServiceApplication.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void verifyGetAllPostsReturnsOk() throws Exception {
        List<PostResponse> posts = Arrays.asList(
                new PostResponse(1L, "Post Title 1", "Post Content 1", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null, 0)
        );
        Mockito.when(postService.getAllPublishedPosts()).thenReturn(posts);

        mockMvc.perform(get("/post"))
                .andExpect(status().isOk());

        verify(postService).getAllPublishedPosts();
    }

    @Test
    public void verifyGetPostByIdReturnsOk() throws Exception {
        Long postId = 1L;
        PostResponse post = new PostResponse(postId, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null, 0);

        Mockito.when(postService.getPostById(postId)).thenReturn(post);

        mockMvc.perform(get("/post/{id}", postId)).andExpect(status().isOk());

        verify(postService).getPostById(postId);
    }

    @Test
    public void verifyGetDraftsFromAuthorReturnsOk() throws Exception {
        String author = "Author 1";
        List<PostResponse> drafts = Arrays.asList(
                new PostResponse(1L, "Draft Title 1", "Draft Content 1", author, LocalDateTime.now(), Category.FOOD, State.DRAFT, null, 0),
                new PostResponse(2L, "Draft Title 2", "Draft Content 2", author, LocalDateTime.now(), Category.FOOD, State.DRAFT, null, 0)
        );

        Mockito.when(postService.getDraftsFromAuthor(author)).thenReturn(drafts);

        mockMvc.perform(get("/post/drafts/{author}", author))
                .andExpect(status().isOk());

        verify(postService).getDraftsFromAuthor(author);
    }

    @Test
    public void verifyCreatePostReturnsCreated() throws Exception {
        PostCreateRequest postCreateRequest = new PostCreateRequest("New Post", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.DRAFT);
        PostResponse postResponse = new PostResponse(1L, "New Post", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.DRAFT, null, 0);

        Mockito.when(postService.addNewPost(postCreateRequest)).thenReturn(postResponse);

        String json = objectMapper.writeValueAsString(postCreateRequest);
        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(postService).addNewPost(postCreateRequest);
    }

    @Test
    public void verifyEditPostReturnsOk() throws Exception {
        Long postId = 1L;
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("Updated Post", "Updated Content", Category.FOOD);
        PostResponse updatedPost = new PostResponse(postId, "Updated Post", "Updated Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null, 0);

        Mockito.when(postService.editPost(postUpdateRequest, postId)).thenReturn(updatedPost);

        String json = objectMapper.writeValueAsString(postUpdateRequest);
        mockMvc.perform(put("/post/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(postService).editPost(postUpdateRequest, postId);
    }

    @Test
    public void verifyPublishPostReturnsOk() throws Exception {
        Long postId = 1L;

        mockMvc.perform(post("/post/{id}/publish", postId)).andExpect(status().isOk());

        verify(postService).publishPost(postId);
    }

    @Test
    public void verifyDeletePostReturnsOk() throws Exception {
        Long postId = 1L;

        mockMvc.perform(delete("/post/{id}", postId)).andExpect(status().isOk());

        verify(postService).deletePostById(postId);
    }

    @Test
    public void verifyGetApprovalReturnsOk() throws Exception {
        Long postId = 1L;
        PostResponse postResponse = new PostResponse(postId, "Post Title", "Post Content", "Author 1", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null, 0);

        Mockito.when(postService.getApproval(postId)).thenReturn(postResponse);

        mockMvc.perform(post("/post/{id}/approval", postId)).andExpect(status().isOk());

        verify(postService).getApproval(postId);
    }
}
