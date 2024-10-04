package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentaireDto {

    private Long id;

    private String contenu;

    @JsonProperty("date_creation")
    private LocalDateTime dateCreation;

    private String utilisateurName;

    private Long annonceId;

    private Long utilisateurId;

}
