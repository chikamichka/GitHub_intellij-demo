package PROJETSI.GestionConferences.services;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.entities.Review;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import PROJETSI.GestionConferences.repositories.ConferenceRepository;
import PROJETSI.GestionConferences.repositories.ReviewRepository;
import PROJETSI.GestionConferences.repositories.SubmissionRepository;
import PROJETSI.GestionConferences.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public User createUser(User user) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(currentUsername).orElse(null);
        if (currentUser != null && currentUser.getInfos().contains("Admin user")) {
            return userRepository.save(user);
        } else {
            throw new IllegalStateException("Only users with 'Admin user' can create new users.");
        }
    }

    public User updateUser(Long id, User user) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(currentUsername).orElse(null);
        if (currentUser != null && currentUser.getInfos().contains("Admin user")) {
            User existingUser = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
            existingUser.setNom(user.getNom());
            existingUser.setPrenom(user.getPrenom());
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            existingUser.setRoles(user.getRoles());
            return userRepository.save(existingUser);
        } else {
            throw new IllegalStateException("Only users with 'Admin user' can update users.");
        }
    }

    public void deleteUser(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(currentUsername).orElse(null);
        if (currentUser != null && currentUser.getInfos().contains("Admin user")) {
            if (!userRepository.existsById(id)) {
                throw new NoSuchElementException("User not found with id " + id);
            }
            userRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Only users with 'Admin user' can delete users.");
        }
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }

    public List<Conference> getConferencesForEditor(Long editorId) {
        User editor = getUserById(editorId);
        if (editor != null && editor.getRoles().contains("EDITOR")) {
            return conferenceRepository.findByEditor(editor);
        } else {
            throw new IllegalStateException("User is not an editor.");
        }
    }

    public List<Submission> getSubmissionsForAuthor(Long authorId) {
        User author = getUserById(authorId);
        if (author != null && author.getRoles().contains("AUTHOR")) {
            return submissionRepository.findByAuteursContaining(author);
        } else {
            throw new IllegalStateException("User is not an author.");
        }
    }

    public List<Review> getReviewsForReviewer(Long reviewerId) {
        User reviewer = getUserById(reviewerId);
        if (reviewer != null && reviewer.getRoles().contains("REVIEWER")) {
            return reviewRepository.findByReviewer(reviewer);
        } else {
            throw new IllegalStateException("User is not a reviewer.");
        }
    }

    public boolean hasAdminRole(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found with username " + username));
        return user.getInfos().contains("Admin user");
    }
}
