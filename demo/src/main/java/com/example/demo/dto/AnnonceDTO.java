package com.example.demo.dto;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AnnonceDTO {
    private Long id;
    private String title;
    private String description;
    private double price;
    private Categorie category;
    private Disponibilite disponibilite;
}
