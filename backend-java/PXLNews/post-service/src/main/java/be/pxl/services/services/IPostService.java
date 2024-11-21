package be.pxl.services.services;

import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.PostCreateRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.PostUpdateRequest;

import java.util.List;

public interface IPostService {
    void addNewPost(PostCreateRequest newPost);

    void editPost(PostUpdateRequest editedPost);

    List<PostResponse> getAllPublishedPosts();

    List<PostResponse> getPostById(Long id);

    void deletePostById(Long id);

    List<PostResponse> getPostByState(State state);

    void getApproval(Long id);
}
