package PROJETSI.GestionConferences.repositories;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConferenceRepository extends CrudRepository<Conference, Long> {
    List<Conference> findByEditor(User editor);
}
