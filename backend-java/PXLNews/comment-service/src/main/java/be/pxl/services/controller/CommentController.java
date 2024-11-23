package be.pxl.services.controller;


import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.domain.dto.CommentUpdate;
import be.pxl.services.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentService commentService;
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @GetMapping("{id}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsForPost(@PathVariable Long id) {
        log.info("Fetching all comments");
        return new ResponseEntity<>(commentService.getAllComments(id), HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id, @RequestBody CommentUpdate commentUpdate) {
        log.info("Updating comment with id: {}", id);
        return new ResponseEntity<>(commentService.updateComment(id, commentUpdate), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.info("Deleting comment with id: {}", id);
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("{postId}")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        log.info("Creating comment for post with id: {}", postId);
        return new ResponseEntity<>(commentService.createComment(postId, commentRequest), HttpStatus.CREATED);
    }
}
