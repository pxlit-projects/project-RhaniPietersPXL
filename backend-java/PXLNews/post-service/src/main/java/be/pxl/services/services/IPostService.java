package be.pxl.services.services;

import be.pxl.services.domain.dto.PostCreateRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.PostUpdateRequest;
import be.pxl.services.domain.dto.ReviewApprovalMessage;

import java.util.List;

public interface IPostService {
    PostResponse addNewPost(PostCreateRequest newPost);

    PostResponse editPost(PostUpdateRequest editedPost, Long id);

    List<PostResponse> getAllPublishedPosts();

    PostResponse getPostById(Long id);

    void deletePostById(Long id);

    PostResponse getApproval(Long id);

    List<PostResponse> getDraftsFromAuthor(String author);

    void publishPost(Long id);

    void setReviewStatus(ReviewApprovalMessage response);
}
