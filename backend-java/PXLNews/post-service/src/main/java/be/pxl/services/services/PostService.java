package be.pxl.services.services;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.PostCreateRequest;
import be.pxl.services.domain.dto.PostRejectRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.PostUpdateRequest;
import be.pxl.services.repository.PostRepository;
import com.ctc.wstx.shaded.msv_core.driver.textui.Debug;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .creationDate(post.getCreationDate())
                .category(post.getCategory())
                .state(post.getState())
                .rejectedMessage(post.getRejectedMessage())
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
        if (post.getState() == State.PENDING_APPROVAL) {
            getApproval(post.getId());
        }
    }

    @Override
    public void editPost(PostUpdateRequest editedPost, Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(editedPost.getTitle());
        post.setContent(editedPost.getContent());
        post.setCategory(editedPost.getCategory());
        postRepository.save(post);
        log.info("Post edited successfully.");
    }

    @Override
    public List<PostResponse> getAllPublishedPosts() {
        List<Post> posts = postRepository.findByState(State.PUBLISHED);
        return posts.stream().map(this::mapToPostResponse).toList();
    }

    @Override
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        return mapToPostResponse(post);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    //TODO andere service
    @Override
    public List<PostResponse> getPostsToApproveNotFromAuthor(String author) {
        log.info("auther {}", author);
        List<Post> posts = postRepository.findAllByStateAndAuthorNot(State.PENDING_APPROVAL, author);
        log.info("count {}", posts.size());
        return posts.stream().map(this::mapToPostResponse).toList();
    }

    @Override
    public void getApproval(Long id) {
        log.info("Sending notification for approval...");
        Post post = postRepository.findById(id).orElseThrow();
        post.setRejectedMessage("");
        post.setState(State.PENDING_APPROVAL);
        postRepository.save(post);
        //TODO send notification
        //TODO controle op status?
    }

    @Override
    public List<PostResponse> getDraftsFromAuthor(String author) {
        log.info("Fetching drafts by author {}", author);
        List<Post> posts = postRepository.findByAuthorAndStateNotIn(author, Arrays.asList(State.PUBLISHED));
        return posts.stream().map(this::mapToPostResponse).toList();
    }

    @Override
    public void publishPost(Long id) {
        //TODO ook bij andere service?
        //TODO controle op status?
        Post post = postRepository.findById(id).orElseThrow();
        post.setState(State.PUBLISHED);
        postRepository.save(post);
    }

    //TODO andere service
    //TODO controle op status?
    @Override
    public void rejectPost(Long id, PostRejectRequest rejectRequest) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setState(State.REJECTED);
        post.setRejectedMessage(rejectRequest.getMessage());
        log.info("Setting message to {}", post.getRejectedMessage());
        postRepository.save(post);
    }

    //TODO andere service
    //TODO controle op status?
    @Override
    public void approvePost(Long id) {
        log.info("Approving post with id {}", id);
        Post post = postRepository.findById(id).orElseThrow();
        log.info("Post before update: id={}, state={}", post.getId(), post.getState());

        post.setState(State.APPROVED);
        post.setRejectedMessage("");

        log.info("Post after update: id={}, new state={}", post.getId(), post.getState());
        postRepository.save(post);
    }
}
