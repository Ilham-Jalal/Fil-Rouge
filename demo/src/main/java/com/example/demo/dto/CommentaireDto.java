package com.example.demo.dto;

import com.example.demo.models.Commentaire;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentaireDto {

    private Long id;

    private String contenu;

    @JsonProperty("date_creation")
    private LocalDateTime dateCreation;

    private String utilisateurName;

    private Long annonceId;

    private Long utilisateurId;

    // Constructor that takes a Commentaire object
    public CommentaireDto(Commentaire commentaire) {
        this.id = commentaire.getId();
        this.contenu = commentaire.getContenu();
        this.dateCreation = commentaire.getDateCreation();
        this.utilisateurName = commentaire.getUtilisateur() != null ? commentaire.getUtilisateur().getUsername() : null;
        this.annonceId = commentaire.getAnnonce() != null ? commentaire.getAnnonce().getId() : null;
        this.utilisateurId = commentaire.getUtilisateur() != null ? commentaire.getUtilisateur().getId() : null;
    }
}
