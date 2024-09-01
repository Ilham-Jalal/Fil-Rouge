package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTransaction;
    private double montant;

    @ManyToOne
    @JoinColumn(name = "vendeur_id")
    private Utilisateur vendeur;

    @ManyToOne
    @JoinColumn(name = "acheteur_id")
    private Utilisateur acheteur;

    @ManyToOne
    @JoinColumn(name = "annonce_id")
    private Annonce annonce;
}
