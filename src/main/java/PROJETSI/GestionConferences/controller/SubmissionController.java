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
    public ResponseEntity<Submission> createSubmission(@RequestBody Submission submission, @RequestParam Long conferenceId, Authentication authentication) {
        String username = authentication.getName();
        Submission createdSubmission = submissionService.createSubmission(submission, conferenceId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubmission);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Submission> updateSubmission(@PathVariable Long id, @RequestBody Submission submission, Authentication authentication) {
        String username = authentication.getName();
        Submission updatedSubmission = submissionService.updateSubmission(id, submission, username);
        return ResponseEntity.ok(updatedSubmission);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping
    public ResponseEntity<List<Submission>> getSubmissionsByAuthor(Authentication authentication) {
        String username = authentication.getName();
        List<Submission> submissions = submissionService.getSubmissionsByAuthor(username);
        return ResponseEntity.ok(submissions);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Submission submission = submissionService.getSubmissionById(id, username);
        return ResponseEntity.ok(submission);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<List<Submission>> getSubmissionsByConference(@PathVariable Long conferenceId, Authentication authentication) {
        String username = authentication.getName();
        List<Submission> submissions = submissionService.getSubmissionsByConference(conferenceId, username);
        return ResponseEntity.ok(submissions);
    }
}
