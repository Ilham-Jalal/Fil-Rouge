package com.example.demo.repositorys;

import com.example.demo.models.Livraison;
import com.example.demo.models.Livreur;
import com.example.demo.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    List<Livraison> findByUtilisateur(Utilisateur utilisateur);
    List<Livraison> findByLivreur(Livreur livreur);
}
