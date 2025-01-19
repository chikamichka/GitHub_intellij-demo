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
import java.util.Objects;


@Service
public class SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;


    public Submission createSubmission(Submission submission, Long conferenceId, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("AUTHOR")) {
            assert conference != null;
            if (currentUser.equals(conference.getEditor())) {
                return null;
            }
            submission.setConference(conference);
            submission.getAuteurs().add(currentUser);
            for (User author : submission.getAuteurs()) {
                User user = userRepository.findById(author.getId()).orElse(null);
                submission.getAuteurs().add(user);
            }
            return submissionRepository.save(submission);
        } else {
            return null;
        }
    }

    public Submission updateSubmission(Long id, Submission submission, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("AUTHOR")) {
            Submission existingSubmission = submissionRepository.findById(id).orElse(null);
            assert existingSubmission != null;
            if (existingSubmission.getAuteurs().contains(currentUser)) {
                existingSubmission.setTitreArticle(submission.getTitreArticle());
                existingSubmission.setResume(submission.getResume());
                existingSubmission.setDocumentPdf(submission.getDocumentPdf());
                return submissionRepository.save(existingSubmission);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Submission> getSubmissionsByAuthor(String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("AUTHOR")) {
            return submissionRepository.findByAuteursContaining(currentUser);
        } else {
            return null;
        }
    }

    public List<Submission> getSubmissionsByConference(Long conferenceId, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId).orElse(null);
        if (currentUser != null && currentUser.equals(Objects.requireNonNull(conference).getEditor())) {
            return submissionRepository.findByConference(conference);
        } else {
            return null;
        }
    }

    public Submission getSubmissionById(Long id, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        Submission submission = submissionRepository.findById(id).orElse(null);
        if (currentUser != null &&
                (Objects.requireNonNull(submission).getAuteurs().contains(currentUser) || submission.getConference().getEditor().equals(currentUser))) {
            return submission;
        } else {
            return null;
        }
    }
}
