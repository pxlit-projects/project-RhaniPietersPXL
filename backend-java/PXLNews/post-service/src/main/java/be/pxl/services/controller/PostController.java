package be.pxl.services.controller;

import be.pxl.services.domain.dto.PostCreateRequest;
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
    public ResponseEntity<PostResponse> editPost(@PathVariable Long id, @RequestBody PostUpdateRequest editedPost) {
        log.info("Editing post: {}", editedPost.getTitle());
        return new ResponseEntity<>(postService.editPost(editedPost, id), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable Long id) {
        log.info("Publishing post with id {}", id);
        postService.publishPost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("Deleting post by id {}", id);
        postService.deletePostById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}