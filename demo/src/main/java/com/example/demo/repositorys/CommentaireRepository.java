package com.example.demo.repositorys;

import com.example.demo.models.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire,Long> {
    List<Commentaire> findByAnnonceId(Long annonceId);

}
