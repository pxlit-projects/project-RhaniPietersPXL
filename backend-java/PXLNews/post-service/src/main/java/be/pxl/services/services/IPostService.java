package be.pxl.services.services;

import be.pxl.services.domain.dto.PostCreateRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.PostUpdateRequest;

import java.util.List;

public interface IPostService {
    void addNewPost(PostCreateRequest newPost);

    void editPost(PostUpdateRequest editedPost, Long id);

    List<PostResponse> getAllPublishedPosts();

    List<PostResponse> getPostById(Long id);

    void deletePostById(Long id);

    List<PostResponse> getPostsToApprove();

    void getApproval(Long id);

    List<PostResponse> getDraftsFromAuthor(String author);
}
