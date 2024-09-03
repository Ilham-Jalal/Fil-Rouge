package com.example.demo.dto;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import lombok.Data;

@Data
public class AnnonceUpdateDTO {
    private String title;
    private String description;
    private double price;
    private Categorie category;
    private Disponibilite disponibilite;
    private Long livraisonId;
}
