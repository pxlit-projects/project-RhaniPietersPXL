package be.pxl.services.services;

import be.pxl.services.client.PostClient;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.ReviewApprovalResponse;
import be.pxl.services.domain.dto.ReviewRequest;
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
    public void setPostForApproval(ReviewRequest request) {
        log.info("Adding post with id {} for approval", request.getId());
        Review review = new Review();
        review.setPostId(request.getId());
        review.setAuthor(request.getAuthor());
        review.setRejectedMessage("");
        reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponse> getPostsToApproveNotFromAuthor(String author) {
        log.info("Fetching posts to review not from author {}", author);
        List<Review> reviews = reviewRepository.findByAuthorNot(author);
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
    public void approvePost(Long id) {
        log.info("Approving post");
        Review review = reviewRepository.findById(id).orElseThrow();
        //sending message to postservice
        ReviewApprovalResponse reviewResponse = new ReviewApprovalResponse();
        reviewResponse.setPostId(review.getPostId());
        reviewResponse.setState(State.APPROVED);
        reviewResponse.setRejectedMessage("");
        rabbitTemplate.convertAndSend("setReview", reviewResponse);

        //TODO send message to notification service

        reviewRepository.delete(review);
        log.info("Review with id {} has been approved and removed from the database", id);
    }


    @Override
    public void rejectPost(Long id, String rejectionMessage) {
        log.info("Rejecting post");
        Review review = reviewRepository.findById(id).orElseThrow();
        ReviewApprovalResponse reviewResponse = new ReviewApprovalResponse();
        reviewResponse.setPostId(review.getPostId());
        reviewResponse.setState(State.REJECTED);
        reviewResponse.setRejectedMessage(rejectionMessage);
        rabbitTemplate.convertAndSend("setReview", reviewResponse);

        //TODO send message to notification service

        reviewRepository.delete(review);
        log.info("Review with id {} has been rejected and removed from the database", id);
    }
}
