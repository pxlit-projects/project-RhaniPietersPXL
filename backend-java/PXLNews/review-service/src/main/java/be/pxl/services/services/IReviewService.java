package be.pxl.services.services;

import be.pxl.services.domain.dto.ReviewResponse;

import java.util.List;

public interface IReviewService {

    List<ReviewResponse> getPostsToApproveNotFromAuthor(String author);
    void approvePost(Long id);
    void rejectPost(Long id, String rejectionMessage);
}
