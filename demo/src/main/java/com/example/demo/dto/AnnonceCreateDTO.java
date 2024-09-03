package com.example.demo.dto;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnonceCreateDTO {
    private String title;
    private String description;
    private double price;
    private Categorie category;
    private Disponibilite disponibilite;
}
