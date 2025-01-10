package PROJETSI.GestionConferences.entities;

import PROJETSI.GestionConferences.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"submission", "reviewer"})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int note;

    @NotNull
    private String commentaires;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReviewStatus etat;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @PostPersist
    @PostUpdate
    public void validateReview() {
        if (submission.getAuteurs().contains(reviewer)) {
            throw new IllegalStateException("A reviewer cannot evaluate their own submission.");
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        if (note < 1 || note > 10) {
            throw new IllegalArgumentException("Note must be between 1 and 10.");
        }
        this.note = note;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public ReviewStatus getEtat() {
        return etat;
    }

    public void setEtat(ReviewStatus etat) {
        this.etat = etat;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }
}
