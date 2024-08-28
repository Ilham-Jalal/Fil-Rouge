package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Livreur extends User {
    private String deliveryZone;

    @OneToMany(mappedBy = "livreur")
    @JsonIgnore
    private List<Livraison> livraisons;
}
