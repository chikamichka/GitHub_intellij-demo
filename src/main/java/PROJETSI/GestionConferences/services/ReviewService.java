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
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new NoSuchElementException("Submission not found with id " + submissionId));

        ReviewAssignment assignment = reviewAssignmentRepository.findBySubmissionAndReviewer(submission, reviewer)
                .orElseThrow(() -> new IllegalStateException("No assignment found for this reviewer and submission."));

        review.setSubmission(submission);
        review.setReviewer(reviewer);
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByReviewer(String username) {
        User reviewer = userRepository.findByUsername(username).orElse(null);
        return reviewRepository.findByReviewer(reviewer);
    }

    public List<Review> getReviewsBySubmission(Long submissionId, String username) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new NoSuchElementException("Submission not found with id " + submissionId));
        User editor = userRepository.findByUsername(username).orElse(null);

        assert editor != null;
        if (!editor.getId().equals(submission.getConference().getEditor().getId())) {
            throw new IllegalStateException("Only the editor can view reviews for this submission.");
        }

        return reviewRepository.findBySubmission(submission);
    }

    public Review updateReview(Long id, Review review, String username) {
        User reviewer = userRepository.findByUsername(username).orElse(null);
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found with id " + id));

        assert reviewer != null;
        if (!existingReview.getReviewer().getId().equals(reviewer.getId())) {
            throw new IllegalStateException("You can only update your own reviews.");
        }

        existingReview.setNote(review.getNote());
        existingReview.setCommentaires(review.getCommentaires());
        existingReview.setEtat(review.getEtat());
        return reviewRepository.save(existingReview);
    }
}
