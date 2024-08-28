package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
public class Utilisateur extends User {
    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<Annonce> ticketAnnonce;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<Message> messages;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<Livraison> livraisons;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<Commentaire> commentaires;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<Favori> favoris;
}