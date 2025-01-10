package PROJETSI.GestionConferences.controller;

import PROJETSI.GestionConferences.entities.ReviewAssignment;
import PROJETSI.GestionConferences.services.ReviewAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review-assignments")
public class ReviewAssignmentController {
    private final ReviewAssignmentService reviewAssignmentService;

    public ReviewAssignmentController(ReviewAssignmentService reviewAssignmentService) {
        this.reviewAssignmentService = reviewAssignmentService;
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping
    public ResponseEntity<ReviewAssignment> assignReviewer(
            @RequestParam("submissionId") Long submissionId,
            @RequestParam("reviewerId") Long reviewerId,
            @RequestParam("conferenceId") Long conferenceId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        ReviewAssignment reviewAssignment = reviewAssignmentService.assignReviewer(submissionId, reviewerId, conferenceId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewAssignment);
    }

    @PreAuthorize("hasRole('REVIEWER')")
    @GetMapping("/reviewer")
    public ResponseEntity<List<ReviewAssignment>> getAssignmentsByReviewer(Authentication authentication) {
        String username = authentication.getName();
        List<ReviewAssignment> assignments = reviewAssignmentService.getAssignmentsByReviewer(username);
        return ResponseEntity.ok(assignments);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<List<ReviewAssignment>> getAssignmentsByConference(@PathVariable Long conferenceId, Authentication authentication) {
        String username = authentication.getName();
        List<ReviewAssignment> assignments = reviewAssignmentService.getAssignmentsByConference(conferenceId, username);
        return ResponseEntity.ok(assignments);
    }
}
