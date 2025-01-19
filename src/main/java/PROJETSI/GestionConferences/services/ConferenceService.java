package PROJETSI.GestionConferences.services;

import PROJETSI.GestionConferences.entities.Conference;
import PROJETSI.GestionConferences.entities.Submission;
import PROJETSI.GestionConferences.entities.User;
import PROJETSI.GestionConferences.repositories.ConferenceRepository;
import PROJETSI.GestionConferences.repositories.SubmissionRepository;
import PROJETSI.GestionConferences.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    public Conference createConference(Conference conference, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("EDITOR")) {
            conference.setCreatorId(currentUser.getId());
            conference.setEditor(currentUser);
            return conferenceRepository.save(conference);
        } else {
            return null;
        }
    }

    public Conference updateConference(Long id, Conference conference, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("EDITOR")) {
            Conference existingConference = conferenceRepository.findById(id).orElse(null);
            if (existingConference == null) {
                return null;
            }
            if (existingConference.getCreatorId().equals(currentUser.getId())) {
                existingConference.setTitre(conference.getTitre());
                existingConference.setDateDebut(conference.getDateDebut());
                existingConference.setDateFin(conference.getDateFin());
                existingConference.setThematique(conference.getThematique());
                existingConference.setEtat(conference.getEtat());
                return conferenceRepository.save(existingConference);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean deleteConference(Long id, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("EDITOR")) {
            Conference existingConference = conferenceRepository.findById(id).orElse(null);
            if (existingConference == null || !existingConference.getCreatorId().equals(currentUser.getId())) {
                return false;
            }
            conferenceRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Conference> getAllConferences() {
        return (List<Conference>) conferenceRepository.findAll();
    }

    public Conference getConferenceById(Long id) {
        return conferenceRepository.findById(id).orElse(null);
    }

    public Conference setWinningSubmission(Long conferenceId, Long submissionId, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId).orElse(null);
        if (conference != null && currentUser != null && currentUser.getId().equals(conference.getEditor().getId())) {
            Submission winningSubmission = submissionRepository.findById(submissionId).orElse(null);
            if (winningSubmission != null) {
                conference.setWinningSubmission(winningSubmission);
                return conferenceRepository.save(conference);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
