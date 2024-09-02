package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Utilisateur extends User {

    @OneToMany(mappedBy = "vendeur")
    @JsonIgnore
    private List<Annonce> ventes;

    @OneToMany(mappedBy = "acheteur")
    @JsonIgnore
    private List<Annonce> achats;

    @OneToMany(mappedBy = "fromUser")
    @JsonIgnore
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "toUser")
    @JsonIgnore
    private List<Message> receivedMessages;

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
