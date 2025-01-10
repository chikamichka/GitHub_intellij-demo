package PROJETSI.GestionConferences.repositories;

import PROJETSI.GestionConferences.entities.Review;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByReviewerId(Long reviewerId);

    List<Review> findByReviewer(User reviewer);

    List<Review> findBySubmission(Submission submission);
}
