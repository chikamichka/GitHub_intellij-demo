package PROJETSI.GestionConferences.controller;

import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.services.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping
    public ResponseEntity<?> createSubmission(@RequestBody Submission submission, @RequestParam Long conferenceId, Authentication authentication) {
        String username = authentication.getName();
        Submission createdSubmission = submissionService.createSubmission(submission, conferenceId, username);
        if (createdSubmission == null) {
            return new ResponseEntity<>("Only users with 'AUTHOR' role can create submissions, and authors who are editors cannot submit to the same conference.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubmission);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable Long id, @RequestBody Submission submission, Authentication authentication) {
        String username = authentication.getName();
        Submission updatedSubmission = submissionService.updateSubmission(id, submission, username);
        if (updatedSubmission == null) {
            return new ResponseEntity<>("Only authors who participated in the submission can update it.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(updatedSubmission);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping
    public ResponseEntity<?> getSubmissionsByAuthor(Authentication authentication) {
        String username = authentication.getName();
        List<Submission> submissions = submissionService.getSubmissionsByAuthor(username);
        if (submissions == null) {
            return new ResponseEntity<>("Only users with 'AUTHOR' role can view submissions.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(submissions);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Submission submission = submissionService.getSubmissionById(id, username);
        if (submission == null) {
            return new ResponseEntity<>("You are not authorized to view this submission.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(submission);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<?> getSubmissionsByConference(@PathVariable Long conferenceId, Authentication authentication) {
        String username = authentication.getName();
        List<Submission> submissions = submissionService.getSubmissionsByConference(conferenceId, username);
        if (submissions == null) {
            return new ResponseEntity<>("Only the editor of the conference can view all submissions.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(submissions);
    }
}
