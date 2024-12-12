package be.pxl.services.services;

import be.pxl.services.client.PostClient;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.NotificationRequest;
import be.pxl.services.domain.dto.ReviewApprovalMessage;
import be.pxl.services.domain.dto.ReviewRequestMessage;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PostClient postClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReviewService reviewService;

    @Captor
    private ArgumentCaptor<ReviewApprovalMessage> reviewApprovalCaptor;

    @Captor
    private ArgumentCaptor<NotificationRequest> notificationRequestCaptor;

    @Test
    public void setPostForApprovalSavesReview() {
        // Arrange
        ReviewRequestMessage requestMessage = new ReviewRequestMessage();
        requestMessage.setPostId(1L);
        requestMessage.setAuthor("JohnDoe");

        // Act
        reviewService.setPostForApproval(requestMessage);

        // Assert
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());

        Review savedReview = reviewCaptor.getValue();
        assertEquals(1L, savedReview.getPostId());
        assertEquals("JohnDoe", savedReview.getAuthor());
        assertEquals("", savedReview.getRejectedMessage());
    }

    @Test
    public void getPostsToApproveNotFromAuthorReturnsCorrectData() {
        // Arrange
        String author = "JohnDoe";
        Review review = new Review();
        review.setId(1L);
        review.setPostId(1L);
        review.setAuthor("JaneDoe");
        review.setRejectedMessage("");

        when(reviewRepository.findByAuthorNot(author)).thenReturn(List.of(review));
        when(postClient.getPostById(1L)).thenReturn(null);

        // Act
        List<ReviewResponse> result = reviewService.getPostsToApproveNotFromAuthor(author);

        // Assert
        assertEquals(1, result.size());
        ReviewResponse response = result.get(0);
        assertEquals(1L, response.getId());
        assertNull(response.getPost());
        assertEquals("", response.getRejectedMessage());
    }

    @Test
    public void approvePostSendsMessagesAndDeletesReview() {
        // Arrange
        Review review = new Review();
        review.setPostId(1L);
        review.setAuthor("JohnDoe");

        when(reviewRepository.findByPostId(1L)).thenReturn(Optional.of(review));

        // Act
        reviewService.approvePost(1L);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(eq("setReview"), reviewApprovalCaptor.capture());
        verify(rabbitTemplate, times(1)).convertAndSend(eq("sendEmail"), notificationRequestCaptor.capture());
        verify(reviewRepository).delete(review);

        ReviewApprovalMessage approvalMessage = reviewApprovalCaptor.getValue();
        assertEquals(1L, approvalMessage.getPostId());
        assertEquals(State.APPROVED.toString(), approvalMessage.getState());
        assertEquals("", approvalMessage.getRejectedMessage());

        NotificationRequest notificationRequest = notificationRequestCaptor.getValue();
        assertEquals("rhanipieters@hotmail.com", notificationRequest.getTo());
        assertEquals("Post approved", notificationRequest.getSubject());
        assertTrue(notificationRequest.getBody().contains("approved successfully"));
    }

    @Test
    public void rejectPostSendsMessagesAndDeletesReview() {
        // Arrange
        String rejectionMessage = "Inappropriate content";
        Review review = new Review();
        review.setPostId(1L);
        review.setAuthor("JohnDoe");

        when(reviewRepository.findByPostId(1L)).thenReturn(Optional.of(review));

        // Act
        reviewService.rejectPost(1L, rejectionMessage);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(eq("setReview"), reviewApprovalCaptor.capture());
        verify(rabbitTemplate, times(1)).convertAndSend(eq("sendEmail"), notificationRequestCaptor.capture());
        verify(reviewRepository).delete(review);

        ReviewApprovalMessage approvalMessage = reviewApprovalCaptor.getValue();
        assertEquals(1L, approvalMessage.getPostId());
        assertEquals(State.REJECTED.toString(), approvalMessage.getState());
        assertEquals(rejectionMessage, approvalMessage.getRejectedMessage());

        NotificationRequest notificationRequest = notificationRequestCaptor.getValue();
        assertEquals("rhanipieters@hotmail.com", notificationRequest.getTo());
        assertEquals("Post rejected", notificationRequest.getSubject());
        assertTrue(notificationRequest.getBody().contains("rejected. Reason: " + rejectionMessage));
    }
}
