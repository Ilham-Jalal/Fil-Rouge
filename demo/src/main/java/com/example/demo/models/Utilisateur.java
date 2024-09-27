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
    private transient List<Annonce> ventes;

    @OneToMany(mappedBy = "acheteur")
    @JsonIgnore
    private transient List<Annonce> achats;

    @OneToMany(mappedBy = "fromUser")
    @JsonIgnore
    private transient List<Message> sentMessages;

    @OneToMany(mappedBy = "toUser")
    @JsonIgnore
    private transient List<Message> receivedMessages;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private transient List<Livraison> livraisons;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private transient List<Commentaire> commentaires;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private transient List<Favori> favoris;
}
