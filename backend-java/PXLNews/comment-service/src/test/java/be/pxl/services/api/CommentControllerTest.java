package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.domain.dto.CommentUpdate;
import be.pxl.services.services.ICommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void verifyGetAllCommentsReturnsOk() throws Exception {
        Long postId = 1L;
        List<CommentResponse> comments = Arrays.asList(
                new CommentResponse(1L, "Title 1", "Content 1", "Author 1", postId),
                new CommentResponse(2L, "Title 2", "Content 2", "Author 2", postId)
        );
        Mockito.when(commentService.getAllComments(postId)).thenReturn(comments);

        mockMvc.perform(get("/comment/{id}", postId))
                .andExpect(status().isOk());

        verify(commentService).getAllComments(postId);
    }

    @Test
    public void verifyCreateCommentReturnsCreated() throws Exception {
        Long postId = 1L;
        CommentRequest commentRequest = new CommentRequest("Title", "This is a comment", "Author 1", postId);
        CommentResponse commentResponse = new CommentResponse(1L, "Title", "This is a comment", "Author 1", postId);

        Mockito.when(commentService.createComment(postId, commentRequest)).thenReturn(commentResponse);

        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/comment/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(commentService).createComment(postId, commentRequest);
    }

    @Test
    public void verifyUpdateCommentReturnsOk() throws Exception {
        Long commentId = 1L;
        CommentUpdate commentUpdate = new CommentUpdate("Updated Title", "Updated Content");
        CommentResponse updatedComment = new CommentResponse(commentId, "Updated Title", "Updated Content", "Author 1", 1L);

        Mockito.when(commentService.updateComment(commentId, commentUpdate)).thenReturn(updatedComment);

        String json = objectMapper.writeValueAsString(commentUpdate);
        mockMvc.perform(put("/comment/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(commentService).updateComment(commentId, commentUpdate);
    }

    @Test
    public void verifyDeleteCommentReturnsNoContent() throws Exception {
        Long commentId = 1L;

        mockMvc.perform(delete("/comment/{id}", commentId))
                .andExpect(status().isNoContent());

        verify(commentService).deleteComment(commentId);
    }

    @Test
    public void verifyGetCommentCountReturnsOk() throws Exception {
        Long postId = 1L;
        int commentCount = 5;

        Mockito.when(commentService.getCommentCount(postId)).thenReturn(commentCount);

        mockMvc.perform(get("/comment/count/{postId}", postId))
                .andExpect(status().isOk());

        verify(commentService).getCommentCount(postId);
    }
}
