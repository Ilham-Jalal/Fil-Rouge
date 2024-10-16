package com.example.demo.dto;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.enums.StatutLivraison;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    private String vendeurName;
    private String vendeurEmail;
    private Long acheteurId;
    private List<String> images;
    private Long livraisonId;
    private StatutLivraison statutLivraison;
}
