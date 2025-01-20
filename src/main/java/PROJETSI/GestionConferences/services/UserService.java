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

import java.util.*;


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
            return null;
        }
    }

    public User updateUser(Long id, User user, String authenticatedUsername) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null || !existingUser.getUsername().equals(authenticatedUsername)) {
            return null;
        }
        existingUser.setNom(user.getNom());
        existingUser.setPrenom(user.getPrenom());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        existingUser.setRoles(user.getRoles());
        return userRepository.save(existingUser);
    }

    public boolean deleteUser(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(currentUsername).orElse(null);
        if (currentUser != null && currentUser.getInfos().contains("Admin user")) {
            if (!userRepository.existsById(id)) {
                return false;
            }
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<Conference> getConferencesForEditor(Long editorId) {
        User editor = getUserById(editorId);
        if (editor != null && editor.getRoles().contains("EDITOR")) {
            return conferenceRepository.findByEditor(editor);
        } else {
            return null;
        }
    }

    public List<Submission> getSubmissionsForAuthor(Long authorId) {
        User author = getUserById(authorId);
        if (author != null && author.getRoles().contains("AUTHOR")) {
            return submissionRepository.findByAuteursContaining(author);
        } else {
            return null;
        }
    }

    public List<Review> getReviewsForReviewer(Long reviewerId) {
        User reviewer = getUserById(reviewerId);
        if (reviewer != null && reviewer.getRoles().contains("REVIEWER")) {
            return reviewRepository.findByReviewer(reviewer);
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getUsersInfo() {
        List<User> users = (List<User>) userRepository.findAll();
        List<Map<String, Object>> usersInfo = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("id", user.getId());
            info.put("nom", user.getNom());
            info.put("prenom", user.getPrenom());
            info.put("username", user.getUsername());
            info.put("infos", user.getInfos());
            info.put("roles", user.getRoles());
            usersInfo.add(info);
        }
        return usersInfo;
    }

    public boolean hasAdminRole(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null && user.getInfos().contains("Admin user");
    }

    public List<Map<String, Object>> getUsersInfos() {
        List<User> users = (List<User>) userRepository.findAll();
        List<Map<String, Object>> usersInfo = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("nom", user.getNom());
            info.put("prenom", user.getPrenom());
            info.put("username", user.getUsername());
            info.put("infos", user.getInfos());
            info.put("roles", user.getRoles());
            usersInfo.add(info);
        }
        return usersInfo;
    }


}
