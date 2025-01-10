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
public class SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;


    public Submission createSubmission(Submission submission, Long conferenceId, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new NoSuchElementException("Conference not found with id " + conferenceId));
        if (currentUser != null && currentUser.getRoles().contains("AUTHOR")) {
            if (currentUser.equals(conference.getEditor())) {
                throw new IllegalStateException("Authors who are editors of the conference cannot submit to it.");
            }
            submission.setConference(conference);
            submission.getAuteurs().add(currentUser);
            for (User author : submission.getAuteurs()) {
                User user = userRepository.findById(author.getId()).orElseThrow(() -> new NoSuchElementException("User not found with id " + author.getId()));
                submission.getAuteurs().add(user);
            }
            return submissionRepository.save(submission);
        } else {
            throw new IllegalStateException("Only users with 'AUTHOR' role can create submissions.");
        }
    }



    public Submission updateSubmission(Long id, Submission submission, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("AUTHOR")) {
            Submission existingSubmission = submissionRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Submission not found with id " + id));
            if (existingSubmission.getAuteurs().contains(currentUser)) {
                existingSubmission.setTitreArticle(submission.getTitreArticle());
                existingSubmission.setResume(submission.getResume());
                existingSubmission.setDocumentPdf(submission.getDocumentPdf());
                return submissionRepository.save(existingSubmission);
            } else {
                throw new IllegalStateException("Only authors who participated in the submission can update it.");
            }
        } else {
            throw new IllegalStateException("Only users with 'AUTHOR' role can update submissions.");
        }
    }

    public List<Submission> getSubmissionsByAuthor(String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null && currentUser.getRoles().contains("AUTHOR")) {
            return submissionRepository.findByAuteursContaining(currentUser);
        } else {
            throw new IllegalStateException("Only users with 'AUTHOR' role can view submissions.");
        }
    }

    public List<Submission> getSubmissionsByConference(Long conferenceId, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new NoSuchElementException("Conference not found with id " + conferenceId));
        if (currentUser != null && currentUser.equals(conference.getEditor())) {
            return submissionRepository.findByConference(conference);
        } else {
            throw new IllegalStateException("Only the editor of the conference can view all submissions.");
        }
    }

    public Submission getSubmissionById(Long id, String username) {
        User currentUser = userRepository.findByUsername(username).orElse(null);
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Submission not found with id " + id));
        if (currentUser != null &&
                (submission.getAuteurs().contains(currentUser) || submission.getConference().getEditor().equals(currentUser))) {
            return submission;
        } else {
            throw new IllegalStateException("You are not authorized to view this submission.");
        }
    }
}
