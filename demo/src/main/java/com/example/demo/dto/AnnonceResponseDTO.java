package com.example.demo.dto;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnonceResponseDTO {
    private Long id;
    private String title;
    private String description;
    private double price;
    private Categorie category;
    private Disponibilite disponibilite;
    private LocalDateTime creationDate;
    private Long vendeurId;
    private Long acheteurId;
    private Long livraisonId;
}
