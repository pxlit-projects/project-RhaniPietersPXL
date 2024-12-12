package be.pxl.services.controller;

import be.pxl.services.ReviewServiceApplication;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.services.IReviewService;
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

@WebMvcTest(ReviewController.class)
@ContextConfiguration(classes = ReviewServiceApplication.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test for findAllByStateAndAuthorNot (GET /review/approval/{author})
    @Test
    public void verifyFindAllByStateAndAuthorNotReturnsOk() throws Exception {
        String author = "Author 1";
        List<ReviewResponse> reviews = Arrays.asList(
                new ReviewResponse(1L, "Rejection Message 1", new PostResponse(1L, "Post Title 1", "Post Content", "Author 2", LocalDateTime.now(), Category.FOOD, State.PUBLISHED, null, 0)),
                new ReviewResponse(2L, "Rejection Message 2", new PostResponse(2L, "Post Title 2", "Post Content", "Author 3", LocalDateTime.now(), Category.FOOD, State.DRAFT, null, 0))
        );

        Mockito.when(reviewService.getPostsToApproveNotFromAuthor(author)).thenReturn(reviews);

        mockMvc.perform(get("/review/approval/{author}", author))
                .andExpect(status().isOk());

        verify(reviewService).getPostsToApproveNotFromAuthor(author);
    }

    // Test for reject (POST /review/{id}/reject)
    @Test
    public void verifyRejectReturnsOk() throws Exception {
        Long postId = 1L;
        String rejectionMessage = "This post has been rejected.";

        mockMvc.perform(post("/review/{id}/reject", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rejectionMessage))
                .andExpect(status().isOk());

        verify(reviewService).rejectPost(postId, rejectionMessage);
    }

    // Test for approve (POST /review/{id}/approve)
    @Test
    public void verifyApproveReturnsOk() throws Exception {
        Long postId = 1L;

        mockMvc.perform(post("/review/{id}/approve", postId))
                .andExpect(status().isOk());

        verify(reviewService).approvePost(postId);
    }
}

