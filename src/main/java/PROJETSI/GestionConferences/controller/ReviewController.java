package PROJETSI.GestionConferences.controller;

import PROJETSI.GestionConferences.entities.Review;
import PROJETSI.GestionConferences.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasRole('REVIEWER')")
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review, @RequestParam Long submissionId, Authentication authentication) {
        String username = authentication.getName();
        Review createdReview = reviewService.createReview(review, submissionId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PreAuthorize("hasRole('REVIEWER')")
    @GetMapping("/reviewer")
    public ResponseEntity<List<Review>> getReviewsByReviewer(Authentication authentication) {
        String username = authentication.getName();
        List<Review> reviews = reviewService.getReviewsByReviewer(username);
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<List<Review>> getReviewsBySubmission(@PathVariable Long submissionId, Authentication authentication) {
        String username = authentication.getName();
        List<Review> reviews = reviewService.getReviewsBySubmission(submissionId, username);
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("hasRole('REVIEWER')")
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review, Authentication authentication) {
        String username = authentication.getName();
        Review updatedReview = reviewService.updateReview(id, review, username);
        return ResponseEntity.ok(updatedReview);
    }
}
