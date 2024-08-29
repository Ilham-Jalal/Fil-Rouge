package com.example.demo.repositorys;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
    List<Annonce> findByUtilisateur_Id(Long id);
    List<Annonce> findByCategory(Categorie category);
    List<Annonce> findByDisponibilite(Disponibilite disponibilite);
    List<Annonce> findByTitleOrDescriptionOrCategoryOrPriceBetween(String title, String description, Categorie category, double minPrice, double maxPrice);

}

