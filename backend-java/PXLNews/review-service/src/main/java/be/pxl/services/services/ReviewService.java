package be.pxl.services.services;

import be.pxl.services.client.PostClient;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.*;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final PostClient postClient;
    private final RabbitTemplate rabbitTemplate;

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    @RabbitListener(queues = "getApproval")
    public void setPostForApproval(ReviewRequestMessage request) {
        log.info("Adding post with id {} for approval", request.getPostId());
        Review review = new Review();
        review.setPostId(request.getPostId());
        review.setAuthor(request.getAuthor());
        review.setRejectedMessage("");
        reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponse> getPostsToApproveNotFromAuthor(String author) {
        log.info("Fetching posts to review not from author {}", author);
        List<Review> reviews = reviewRepository.findByAuthorNot(author);
        log.info("Found {} posts to review", reviews.size());
        for (Review review : reviews) {
            log.info("Post with id {} is pending approval", review.getPostId());
        }
        return reviews.stream()
                .map(review -> {
                    PostResponse postResponse = postClient.getPostById(review.getPostId());
                    return ReviewResponse.builder()
                            .id(review.getId())
                            .post(postResponse)
                            .rejectedMessage(review.getRejectedMessage()).build();
                }).toList();
    }

    @Override
    public void approvePost(Long postId) {
        log.info("Approving post");
        Review review = reviewRepository.findByPostId(postId).orElseThrow();
        //sending message to postservice
        ReviewApprovalMessage reviewResponse = new ReviewApprovalMessage();
        reviewResponse.setPostId(review.getPostId());
        reviewResponse.setState(String.valueOf(State.APPROVED));
        reviewResponse.setRejectedMessage("");
        rabbitTemplate.convertAndSend("setReview", reviewResponse);

        log.info("sending email to author");


        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to("rhanipieters@hotmail.com")
                .subject("Post approved")
                .body("Your post has been approved successfully.")
                .build();
        rabbitTemplate.convertAndSend("sendEmail", notificationRequest);

        reviewRepository.delete(review);
        log.info("Review with postId {} has been approved and removed from the database", postId);
    }


    @Override
    public void rejectPost(Long postId, String rejectionMessage) {
        log.info("Rejecting post");
        Review review = reviewRepository.findByPostId(postId).orElseThrow();
        ReviewApprovalMessage reviewResponse = new ReviewApprovalMessage();

        reviewResponse.setPostId(postId);
        reviewResponse.setState(String.valueOf(State.REJECTED));
        reviewResponse.setRejectedMessage(rejectionMessage);
        log.info("setting id to {} and state to {}, rejected message to {}", reviewResponse.getPostId(), reviewResponse.getState(), reviewResponse.getRejectedMessage());

        rabbitTemplate.convertAndSend("setReview", reviewResponse);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to("rhanipieters@hotmail.com")
                .subject("Post rejected")
                .body("Your post has been rejected. Reason: " + rejectionMessage)
                .build();
        rabbitTemplate.convertAndSend("sendEmail", notificationRequest);

        reviewRepository.delete(review);
        log.info("Review with postId {} has been rejected and removed from the database", postId);
    }
}
