package be.pxl.services.controller;

import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.services.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;

    @GetMapping("/approval/{author}")
    public ResponseEntity<List<ReviewResponse>> findAllByStateAndAuthorNot(@PathVariable String author) {
        return new ResponseEntity<>(reviewService.getPostsToApproveNotFromAuthor(author), HttpStatus.OK);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestBody String rejectionMessage) {
        reviewService.rejectPost(id, rejectionMessage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        reviewService.approvePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
