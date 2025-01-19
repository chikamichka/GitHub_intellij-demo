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
    public ResponseEntity<?> assignReviewer(
            @RequestParam("submissionId") Long submissionId,
            @RequestParam("reviewerId") Long reviewerId,
            @RequestParam("conferenceId") Long conferenceId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        ReviewAssignment reviewAssignment = reviewAssignmentService.assignReviewer(submissionId, reviewerId, conferenceId, username);
        if (reviewAssignment == null) {
            return new ResponseEntity<>("Submission, Reviewer, or Conference not found or assignment not permitted.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewAssignment);
    }

    @PreAuthorize("hasRole('REVIEWER')")
    @GetMapping("/reviewer")
    public ResponseEntity<?> getAssignmentsByReviewer(Authentication authentication) {
        String username = authentication.getName();
        List<ReviewAssignment> assignments = reviewAssignmentService.getAssignmentsByReviewer(username);
        if (assignments == null) {
            return new ResponseEntity<>("Reviewer not found or invalid credentials.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(assignments);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<?> getAssignmentsByConference(@PathVariable Long conferenceId, Authentication authentication) {
        String username = authentication.getName();
        List<ReviewAssignment> assignments = reviewAssignmentService.getAssignmentsByConference(conferenceId, username);
        if (assignments == null) {
            return new ResponseEntity<>("Conference not found or you are not authorized to view assignments.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(assignments);
    }
}
