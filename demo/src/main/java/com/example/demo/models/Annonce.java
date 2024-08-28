package com.example.demo.models;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categorie category;

    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private Disponibilite disponibilite;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;

    @OneToMany(mappedBy = "annonce")
    @JsonIgnore
    private List<Commentaire> commentaires;

    @OneToMany(mappedBy = "annonce")
    @JsonIgnore
    private List<Favori> favoris;
}
