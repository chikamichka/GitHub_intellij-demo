package PROJETSI.GestionConferences.services;

import PROJETSI.GestionConferences.entities.Review;
import PROJETSI.GestionConferences.entities.ReviewAssignment;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import PROJETSI.GestionConferences.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewAssignmentRepository reviewAssignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    public Review createReview(Review review, Long submissionId, String username) {
        User reviewer = userRepository.findByUsername(username).orElse(null);
        Submission submission = submissionRepository.findById(submissionId).orElse(null);
        ReviewAssignment assignment = reviewAssignmentRepository.findBySubmissionAndReviewer(submission, reviewer).orElse(null);

        if (reviewer != null && submission != null && assignment != null) {
            review.setSubmission(submission);
            review.setReviewer(reviewer);
            return reviewRepository.save(review);
        } else {
            return null;
        }
    }

    public List<Review> getReviewsByReviewer(String username) {
        User reviewer = userRepository.findByUsername(username).orElse(null);
        return reviewer != null ? reviewRepository.findByReviewer(reviewer) : null;
    }

    public List<Review> getReviewsBySubmission(Long submissionId, String username) {
        Submission submission = submissionRepository.findById(submissionId).orElse(null);
        User editor = userRepository.findByUsername(username).orElse(null);

        if (submission != null && editor != null && editor.getId().equals(submission.getConference().getEditor().getId())) {
            return reviewRepository.findBySubmission(submission);
        } else {
            return null;
        }
    }

    public Review updateReview(Long id, Review review, String username) {
        User reviewer = userRepository.findByUsername(username).orElse(null);
        Review existingReview = reviewRepository.findById(id).orElse(null);

        if (existingReview != null && reviewer != null && existingReview.getReviewer().getId().equals(reviewer.getId())) {
            existingReview.setNote(review.getNote());
            existingReview.setCommentaires(review.getCommentaires());
            existingReview.setEtat(review.getEtat());
            return reviewRepository.save(existingReview);
        } else {
            return null;
        }
    }
}
