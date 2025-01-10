package PROJETSI.GestionConferences.controller;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.entities.Review;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import PROJETSI.GestionConferences.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("@userService.hasAdminRole(authentication.name)")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PreAuthorize("@userService.hasAdminRole(authentication.name)")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User not found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@userService.hasAdminRole(authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User not found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/conferences")
    public ResponseEntity<List<Conference>> getConferencesForEditor(@PathVariable Long id) {
        List<Conference> conferences = userService.getConferencesForEditor(id);
        return ResponseEntity.ok(conferences);
    }

    @GetMapping("/{id}/submissions")
    public ResponseEntity<List<Submission>> getSubmissionsForAuthor(@PathVariable Long id) {
        List<Submission> submissions = userService.getSubmissionsForAuthor(id);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getReviewsForReviewer(@PathVariable Long id) {
        List<Review> reviews = userService.getReviewsForReviewer(id);
        return ResponseEntity.ok(reviews);
    }
}
