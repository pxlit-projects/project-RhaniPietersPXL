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
public class PostController {
    private final IPostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private boolean isRoleNotValid(String role) {
        if (!role.equals("redacteur")) {
            log.warn("Unauthorized access attempt with role: {}", role);
            return true;
        }
        return false;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPublished(@RequestHeader("role") String role) {
        if (role.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("Fetching all posts");
        return new ResponseEntity<>(postService.getAllPublishedPosts(), HttpStatus.OK);
    }

    //NO Request Header, request from review-service
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
        log.info("Fetching posts by id {}", id);
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @GetMapping("/drafts/{author}")
    public ResponseEntity<List<PostResponse>> getDraftsFromAuthor(@PathVariable String author, @RequestHeader("user") String user) {
        if (!user.equals(author)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("Fetching drafts by author {}", author);
        return new ResponseEntity<>(postService.getDraftsFromAuthor(author), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PostResponse> add(@RequestBody PostCreateRequest newPost, @RequestHeader("role") String role) {
        if (isRoleNotValid(role)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("Adding new post: {}", newPost.getTitle());
        return new ResponseEntity<>(postService.addNewPost(newPost), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/approval")
    public ResponseEntity<PostResponse> getApproval(@PathVariable Long id, @RequestHeader("role") String role) {
        if (isRoleNotValid(role)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("Ask approval for post with id {}", id);
        return new ResponseEntity<>(postService.getApproval(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> editPost(@PathVariable Long id, @RequestBody PostUpdateRequest editedPost, @RequestHeader("role") String role) {
        if (isRoleNotValid(role)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("Editing post: {}", editedPost.getTitle());
        return new ResponseEntity<>(postService.editPost(editedPost, id), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable Long id, @RequestHeader("role") String role) {
        if (isRoleNotValid(role)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("Publishing post with id {}", id);
        postService.publishPost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}