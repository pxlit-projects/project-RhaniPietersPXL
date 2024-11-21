package be.pxl.services.services;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.PostCreateRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.PostUpdateRequest;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .creationDate(post.getCreationDate())
                .category(post.getCategory())
                .build();
    }

    @Override
    public void addNewPost(PostCreateRequest newPost) {
        log.info("Adding new post: {}", newPost.getTitle());
        Post post = Post.builder()
                .title(newPost.getTitle())
                .content(newPost.getContent())
                .author(newPost.getAuthor())
                .category(newPost.getCategory())
                .creationDate(newPost.getCreationDate())
                .state(newPost.getState())
                .build();

        postRepository.save(post);
        log.info("Post saved successfully.");
        if(post.getState() == State.PENDING_APPROVAL) {
            getApproval(post.getId());
        }
    }

    @Override
    public void editPost(PostUpdateRequest editedPost, Long id) {

    }

    @Override
    public List<PostResponse> getAllPublishedPosts() {
        return List.of();
    }

    @Override
    public List<PostResponse> getPostById(Long id) {
        return List.of();
    }

    @Override
    public void deletePostById(Long id) {

    }

    @Override
    public List<PostResponse> getPostsToApprove() {
        return List.of();
    }

    @Override
    public void getApproval(Long id) {
        log.info("Sending notification for approval...");
        //TODO send notification
    }

    @Override
    public List<PostResponse> getDraftsFromAuthor(String author) {
        return List.of();
    }
}
