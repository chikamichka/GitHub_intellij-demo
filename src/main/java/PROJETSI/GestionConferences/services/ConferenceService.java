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
            throw new IllegalStateException("Only users with 'EDITOR' role can create conferences.");
        }
    }

    public Conference updateConference(Long id, Conference conference, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("EDITOR")) {
            Conference existingConference = conferenceRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Conference not found with id " + id));
            if (existingConference.getCreatorId().equals(currentUser.getId())) {
                existingConference.setTitre(conference.getTitre());
                existingConference.setDateDebut(conference.getDateDebut());
                existingConference.setDateFin(conference.getDateFin());
                existingConference.setThematique(conference.getThematique());
                existingConference.setEtat(conference.getEtat());
                return conferenceRepository.save(existingConference);
            } else {
                throw new IllegalStateException("Only the creator can update this conference.");
            }
        } else {
            throw new IllegalStateException("Only users with 'EDITOR' role can update conferences.");
        }
    }

    public void deleteConference(Long id, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("EDITOR")) {
            Conference existingConference = conferenceRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Conference not found with id " + id));
            if (existingConference.getCreatorId().equals(currentUser.getId())) {
                conferenceRepository.deleteById(id);
            } else {
                throw new IllegalStateException("Only the creator can delete this conference.");
            }
        } else {
            throw new IllegalStateException("Only users with 'EDITOR' role can delete conferences.");
        }
    }

    public List<Conference> getAllConferences() {
        return (List<Conference>) conferenceRepository.findAll();
    }

    public Conference getConferenceById(Long id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Conference not found with id " + id));
    }

    public Conference setWinningSubmission(Long conferenceId, Long submissionId, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new NoSuchElementException("Conference not found with id " + conferenceId));
        if (currentUser != null && currentUser.getId().equals(conference.getEditor().getId())) {
            Submission winningSubmission = submissionRepository.findById(submissionId)
                    .orElseThrow(() -> new NoSuchElementException("Submission not found with id " + submissionId));
            conference.setWinningSubmission(winningSubmission);
            return conferenceRepository.save(conference);
        } else {
            throw new IllegalStateException("Only the editor can set the winning submission for this conference.");
        }
    }
}
