package com.example.demo.models;

import com.example.demo.enums.StatutLivraison;
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
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String adresseVendeur;
    @Column(nullable = false)
    private String adresseAcheteur;
    private double montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutLivraison statut;

    private LocalDateTime dateLivraison;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @OneToMany(mappedBy = "livraison")
    @JsonIgnore
    private List<Annonce> annonces;
}
