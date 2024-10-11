package com.example.demo.models;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ElementCollection
    @CollectionTable(name = "annonce_images", joinColumns = @JoinColumn(name = "annonce_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Enumerated(EnumType.STRING)
    private Disponibilite disponibilite;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "vendeur_id")
    private Utilisateur vendeur;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "acheteur_id")
    private Utilisateur acheteur;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;

    @OneToMany(mappedBy = "annonce", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Commentaire> commentaires;

    @OneToMany(mappedBy = "annonce", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Favori> favoris;
}
