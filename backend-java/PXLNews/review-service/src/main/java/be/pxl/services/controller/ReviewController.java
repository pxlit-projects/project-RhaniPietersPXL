package be.pxl.services.controller;

import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.services.IReviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    private boolean isRoleNotValid(String role) {
        if (!role.equals("redacteur")) {
            log.warn("Unauthorized access attempt with role: {}", role);
            return true;
        }
        return false;
    }

    @GetMapping("/approval/{author}")
    public ResponseEntity<List<ReviewResponse>> findAllByStateAndAuthorNot(@PathVariable String author, @RequestHeader("role") String role) {
        if (isRoleNotValid(role)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(reviewService.getPostsToApproveNotFromAuthor(author), HttpStatus.OK);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestBody String rejectionMessage, @RequestHeader("role") String role) {
        if (isRoleNotValid(role)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        reviewService.rejectPost(id, rejectionMessage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id, @RequestHeader("role") String role) {
        if (isRoleNotValid(role)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        reviewService.approvePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
