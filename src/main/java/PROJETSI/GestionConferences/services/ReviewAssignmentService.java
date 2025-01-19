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
        Submission submission = submissionRepository.findById(submissionId).orElse(null);
        User reviewer = userRepository.findById(reviewerId).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId).orElse(null);

        if (editor != null && submission != null && reviewer != null && conference != null) {
            if (!editor.getId().equals(conference.getEditor().getId()) ||
                    submission.getAuteurs().contains(reviewer) ||
                    !reviewer.getRoles().contains("REVIEWER")) {
                return null;
            }
            ReviewAssignment reviewAssignment = new ReviewAssignment(submission, reviewer, conference);
            return reviewAssignmentRepository.save(reviewAssignment);
        } else {
            return null;
        }
    }

    public List<ReviewAssignment> getAssignmentsByReviewer(String username) {
        User reviewer = userRepository.findByUsername(username).orElse(null);
        return reviewer != null ? reviewAssignmentRepository.findByReviewer(reviewer) : null;
    }

    public List<ReviewAssignment> getAssignmentsByConference(Long conferenceId, String username) {
        User editor = userRepository.findByUsername(username).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId).orElse(null);

        if (editor != null && conference != null && editor.getId().equals(conference.getEditor().getId())) {
            return reviewAssignmentRepository.findByConference(conference);
        } else {
            return null;
        }
    }
}
