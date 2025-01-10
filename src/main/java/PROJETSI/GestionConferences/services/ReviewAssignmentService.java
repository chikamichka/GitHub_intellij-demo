package PROJETSI.GestionConferences.services;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.entities.ReviewAssignment;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import PROJETSI.GestionConferences.repositories.ConferenceRepository;
import PROJETSI.GestionConferences.repositories.ReviewAssignmentRepository;
import PROJETSI.GestionConferences.repositories.SubmissionRepository;
import PROJETSI.GestionConferences.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewAssignmentService {
    @Autowired
    private ReviewAssignmentRepository reviewAssignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    public ReviewAssignment assignReviewer(Long submissionId, Long reviewerId, Long conferenceId, String username) {
        User editor = userRepository.findByUsername(username).orElse(null);
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new NoSuchElementException("Submission not found with id " + submissionId));
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new NoSuchElementException("Reviewer not found with id " + reviewerId));
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new NoSuchElementException("Conference not found with id " + conferenceId));

        if (!editor.getId().equals(conference.getEditor().getId())) {
            throw new IllegalStateException("Only the editor can assign reviewers for this conference.");
        }

        if (submission.getAuteurs().contains(reviewer)) {
            throw new IllegalStateException("A reviewer cannot be assigned to their own submission.");
        }

        if (!reviewer.getRoles().contains("REVIEWER")) {
            throw new IllegalStateException("User does not have the REVIEWER role.");
        }

        ReviewAssignment reviewAssignment = new ReviewAssignment(submission, reviewer, conference);
        return reviewAssignmentRepository.save(reviewAssignment);
    }

    public List<ReviewAssignment> getAssignmentsByReviewer(String username) {
        User reviewer = userRepository.findByUsername(username).orElse(null);
        return reviewAssignmentRepository.findByReviewer(reviewer);
    }

    public List<ReviewAssignment> getAssignmentsByConference(Long conferenceId, String username) {
        User editor = userRepository.findByUsername(username).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new NoSuchElementException("Conference not found with id " + conferenceId));

        if (!editor.getId().equals(conference.getEditor().getId())) {
            throw new IllegalStateException("Only the editor can view assignments for this conference.");
        }

        return reviewAssignmentRepository.findByConference(conference);
    }
}
