package PROJETSI.GestionConferences.controller;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.entities.Review;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import PROJETSI.GestionConferences.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<List<Map<String, Object>>> getUsersInfos() {
        List<Map<String, Object>> usersInfo = userService.getUsersInfo();
        return ResponseEntity.ok(usersInfo);
    }

    @PreAuthorize("@userService.hasAdminRole(authentication.name)")
    @PostMapping
    public Object createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            return new ResponseEntity<>("Only users with 'Admin user' can create new users.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user, Authentication authentication) {
        String authenticatedUsername = authentication.getName();
        try {
            User updatedUser = userService.updateUser(id, user, authenticatedUsername);
            if (updatedUser == null) {
                return new ResponseEntity<>("User not found or you are not authorized to update this account.", HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User not found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@userService.hasAdminRole(authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        String authenticatedUsername = authentication.getName();
        if (!userService.hasAdminRole(authenticatedUsername)) {
            return new ResponseEntity<>("You are not authorized to delete users.", HttpStatus.FORBIDDEN);
        }
        boolean isDeleted = userService.deleteUser(id);
        if (!isDeleted) {
            return new ResponseEntity<>("User not found with id " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("User Deleted with id" +id ,HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@userService.hasAdminRole(authentication.name)")
    @GetMapping("/details")
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        String authenticatedUsername = authentication.getName();
        if (!userService.hasAdminRole(authenticatedUsername)) {
            return new ResponseEntity<>("You are not authorized to view all users details.", HttpStatus.FORBIDDEN);
        }
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("@userService.hasAdminRole(authentication.name)")
    @GetMapping("/{id}")
    public Object getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>("User not found with id " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> getUsersInfo() {
        List<Map<String, Object>> usersInfo = userService.getUsersInfo();
        return ResponseEntity.ok(usersInfo);
    }

    @GetMapping("/{id}/conferences")
    public Object getConferencesForEditor(@PathVariable Long id) {
        List<Conference> conferences = userService.getConferencesForEditor(id);
        if (conferences == null) {
            return new ResponseEntity<>("User is not an editor.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(conferences);
    }

    @GetMapping("/{id}/submissions")
    public Object getSubmissionsForAuthor(@PathVariable Long id) {
        List<Submission> submissions = userService.getSubmissionsForAuthor(id);
        if (submissions == null) {
            return new ResponseEntity<>("User is not an author.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/{id}/reviews")
    public Object getReviewsForReviewer(@PathVariable Long id) {
        List<Review> reviews = userService.getReviewsForReviewer(id);
        if (reviews == null) {
            return new ResponseEntity<>("User is not a reviewer.", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(reviews);
    }
}
