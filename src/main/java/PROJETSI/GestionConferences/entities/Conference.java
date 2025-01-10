package PROJETSI.GestionConferences.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"editor", "soumissions"})
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String titre;

    @NotNull
    private Date dateDebut;

    @NotNull
    private Date dateFin;

    @NotNull
    private String thematique;

    private String etat;

    @ManyToOne
    @JoinColumn(name = "editor_id")
    private User editor;

    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL)
    private Set<Submission> soumissions;

    @OneToOne
    @JoinColumn(name = "winning_submission_id")
    private Submission winningSubmission;

    @NotNull
    @Column(name = "creator_id")
    private Long creatorId;





    // Getters and setters for creatorId
    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    // Other getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getThematique() {
        return thematique;
    }

    public void setThematique(String thematique) {
        this.thematique = thematique;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Set<Submission> getSoumissions() {
        return soumissions;
    }

    public void setSoumissions(Set<Submission> soumissions) {
        this.soumissions = soumissions;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public Submission getWinningSubmission() {
        return winningSubmission;
    }

    public void setWinningSubmission(Submission winningSubmission) {
        this.winningSubmission = winningSubmission;
    }
}
