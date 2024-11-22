package be.pxl.services.controller;

import be.pxl.services.domain.dto.PostCreateRequest;
import be.pxl.services.domain.dto.PostRejectRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.domain.dto.PostUpdateRequest;
import be.pxl.services.services.IPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {
    private final IPostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPublished() {
        log.info("Fetching all posts");
        return new ResponseEntity<>(postService.getAllPublishedPosts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
        log.info("Fetching posts by id {}", id);
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @GetMapping("/approval/{author}")
    public ResponseEntity<List<PostResponse>> findAllByStateAndAuthorNot(@PathVariable String author) {
        log.info("Fetching posts to approve");
        return new ResponseEntity<>(postService.getPostsToApproveNotFromAuthor(author), HttpStatus.OK);
    }

    @GetMapping("/drafts/{author}")
    public ResponseEntity<List<PostResponse>> getDraftsFromAuthor(@PathVariable String author) {
        log.info("Fetching drafts by author {}", author);
        return new ResponseEntity<>(postService.getDraftsFromAuthor(author), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody PostCreateRequest newPost) {
        log.info("Adding new post: {}", newPost.getTitle());
        postService.addNewPost(newPost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/{id}/approval")
    public ResponseEntity<Void> getApproval(@PathVariable Long id) {
        log.info("Ask approval for post with id {}", id);
        postService.getApproval(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editPost(@PathVariable Long id, @RequestBody PostUpdateRequest editedPost) {
        log.info("Editing post: {}", editedPost.getTitle());
        postService.editPost(editedPost, id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        log.info("Approving post with id {}", id);
        postService.approvePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable Long id) {
        log.info("Publishing post with id {}", id);
        postService.publishPost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestBody PostRejectRequest rejectRequest) {
        log.info("Rejecting post with id {}", id);
        postService.rejectPost(id, rejectRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("Deleting post by id {}", id);
        postService.deletePostById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}