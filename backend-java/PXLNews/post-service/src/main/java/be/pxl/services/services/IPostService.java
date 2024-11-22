package be.pxl.services.services;

import be.pxl.services.domain.dto.PostCreateRequest;
import be.pxl.services.domain.dto.PostRejectRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.PostUpdateRequest;

import java.util.List;

public interface IPostService {
    void addNewPost(PostCreateRequest newPost);

    void editPost(PostUpdateRequest editedPost, Long id);

    List<PostResponse> getAllPublishedPosts();

    PostResponse getPostById(Long id);

    void deletePostById(Long id);

    List<PostResponse> getPostsToApproveNotFromAuthor(String author);

    void getApproval(Long id);

    List<PostResponse> getDraftsFromAuthor(String author);

    void publishPost(Long id);

    void rejectPost(Long id, PostRejectRequest rejectRequest);

    void approvePost(Long id);
}
