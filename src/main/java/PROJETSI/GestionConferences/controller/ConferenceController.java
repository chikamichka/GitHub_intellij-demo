package PROJETSI.GestionConferences.controller;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.services.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {
    private final ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping
    public Object createConference(@RequestBody Conference conference, Authentication authentication) {
        String username = authentication.getName();
        Conference createdConference = conferenceService.createConference(conference, username);
        if (createdConference == null) {
            return new ResponseEntity<>("Only users with 'EDITOR' role can create conferences.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConference);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PutMapping("/{id}")
    public Object updateConference(@PathVariable Long id, @RequestBody Conference conference, Authentication authentication) {
        String username = authentication.getName();
        Conference updatedConference = conferenceService.updateConference(id, conference, username);
        if (updatedConference == null) {
            return new ResponseEntity<>("Conference not found with id " + id + " or not permitted to update.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(updatedConference);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConference(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        boolean isDeleted = conferenceService.deleteConference(id, username);
        if (!isDeleted) {
            return new ResponseEntity<>("Conference not found with id " + id + " or not permitted to delete.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Conference>> getAllConferences() {
        List<Conference> conferences = conferenceService.getAllConferences();
        return ResponseEntity.ok(conferences);
    }

    @GetMapping("/{id}")
    public Object getConferenceById(@PathVariable Long id) {
        Conference conference = conferenceService.getConferenceById(id);
        if (conference == null) {
            return new ResponseEntity<>("Conference not found with id " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(conference);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/{id}/set-winning-submission")
    public Object setWinningSubmission(@PathVariable Long id, @RequestParam Long submissionId, Authentication authentication) {
        String username = authentication.getName();
        Conference updatedConference = conferenceService.setWinningSubmission(id, submissionId, username);
        if (updatedConference == null) {
            return new ResponseEntity<>("Conference or Submission not found or not permitted to set winning submission.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(updatedConference);
    }
}
