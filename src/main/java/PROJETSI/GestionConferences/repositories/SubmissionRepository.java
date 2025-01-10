package PROJETSI.GestionConferences.repositories;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {
    List<Submission> findByAuteursContaining(User author);
    List<Submission> findByConference(Conference conference);
}
