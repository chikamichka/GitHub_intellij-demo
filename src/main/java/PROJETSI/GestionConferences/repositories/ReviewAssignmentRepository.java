package PROJETSI.GestionConferences.repositories;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.entities.ReviewAssignment;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewAssignmentRepository extends CrudRepository<ReviewAssignment, Long> {
    List<ReviewAssignment> findByReviewer(User reviewer);
    List<ReviewAssignment> findByConference(Conference conference);
    Optional<ReviewAssignment> findBySubmissionAndReviewer(Submission submission, User reviewer);
}

