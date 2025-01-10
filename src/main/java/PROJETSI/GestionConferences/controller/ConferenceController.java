package PROJETSI.GestionConferences.controller;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.services.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {
    private final ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping
    public ResponseEntity<Conference> createConference(@RequestBody Conference conference, Authentication authentication) {
        String username = authentication.getName();
        Conference createdConference = conferenceService.createConference(conference, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConference);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Conference> updateConference(@PathVariable Long id, @RequestBody Conference conference, Authentication authentication) {
        String username = authentication.getName();
        Conference updatedConference = conferenceService.updateConference(id, conference, username);
        return ResponseEntity.ok(updatedConference);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConference(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        try {
            conferenceService.deleteConference(id, username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Conference not found with id " + id, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public ResponseEntity<List<Conference>> getAllConferences() {
        List<Conference> conferences = conferenceService.getAllConferences();
        return ResponseEntity.ok(conferences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conference> getConferenceById(@PathVariable Long id) {
        Conference conference = conferenceService.getConferenceById(id);
        return ResponseEntity.ok(conference);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/{id}/set-winning-submission")
    public ResponseEntity<Conference> setWinningSubmission(@PathVariable Long id, @RequestParam Long submissionId, Authentication authentication) {
        String username = authentication.getName();
        Conference updatedConference = conferenceService.setWinningSubmission(id, submissionId, username);
        return ResponseEntity.ok(updatedConference);
    }
}
