package be.pxl.services.services;

import be.pxl.services.Domain.ReviewApprovalMessage;
import be.pxl.services.Domain.ReviewRequestMessage;
import be.pxl.services.client.CommentClient;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.State;
import be.pxl.services.domain.dto.*;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private final CommentClient commentClient;

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
    public PostResponse addNewPost(PostCreateRequest newPost) {
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
        return mapToPostResponse(post);
    }

    @Override
    public PostResponse editPost(PostUpdateRequest editedPost, Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(editedPost.getTitle());
        post.setContent(editedPost.getContent());
        post.setCategory(editedPost.getCategory());
        postRepository.save(post);
        log.info("Post edited successfully.");
        return mapToPostResponse(post);
    }

    @Override
    public List<PostResponse> getAllPublishedPosts() {
        List<Post> posts = postRepository.findByState(State.PUBLISHED);
        List<PostResponse> postResponses = posts.stream().map(this::mapToPostResponse).toList();
        for (PostResponse post : postResponses) {
            post.setCommentCount(commentClient.getCommentCountForPost(post.getId()));
        }
        return postResponses;
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

    @Override
    public List<PostResponse> getDraftsFromAuthor(String author) {
        log.info("Fetching drafts by author {}", author);
        List<Post> posts = postRepository.findByAuthorAndStateNotIn(author, List.of(State.PUBLISHED));
        return posts.stream().map(this::mapToPostResponse).toList();
    }

    @Override
    public PostResponse getApproval(Long id) {
        log.info("Sending template for approval {} ...", id);
        Post post = postRepository.findById(id).orElseThrow();
        post.setRejectedMessage("");
        post.setState(State.PENDING_APPROVAL);
        postRepository.save(post);
        ReviewRequestMessage reviewRequest = ReviewRequestMessage.builder()
                .author(post.getAuthor())
                .postId(post.getId())
                .build();
        rabbitTemplate.convertAndSend("getApproval", reviewRequest);
        return mapToPostResponse(post);
    }

    @RabbitListener(queues = "setReview")
    public void setReviewStatus(ReviewApprovalMessage response) {
        log.info("Changing state of post with id {} after review to ", response.getPostId());
        Post post = postRepository.findById(response.getPostId()).orElseThrow();
        post.setState(State.valueOf(response.getState()));
        post.setRejectedMessage(response.getRejectedMessage());
        postRepository.save(post);
    }

    @Override
    public void publishPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setState(State.PUBLISHED);
        postRepository.save(post);
    }
}
