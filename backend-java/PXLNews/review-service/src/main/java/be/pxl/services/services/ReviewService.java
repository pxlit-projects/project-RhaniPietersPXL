package be.pxl.services.services;

import be.pxl.services.Domain.ReviewApprovalMessage;
import be.pxl.services.Domain.ReviewRequestMessage;
import be.pxl.services.client.PostClient;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final PostClient postClient;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


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

        //TODO send message to notification service

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
        rabbitTemplate.convertAndSend("setReview", reviewResponse);

        //TODO send message to notification service

        reviewRepository.delete(review);
        log.info("Review with postId {} has been rejected and removed from the database", postId);
    }
}
