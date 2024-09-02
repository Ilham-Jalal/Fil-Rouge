package com.example.demo.repositorys;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
//    List<Annonce> findByUtilisateur_Id(Long id);
    List<Annonce> findByCategory(Categorie category);
    List<Annonce> findByDisponibilite(Disponibilite disponibilite);
    @Query("SELECT a FROM Annonce a WHERE " +
            "(?1 IS NULL OR a.title LIKE %?1%) AND " +
            "(?2 IS NULL OR a.description LIKE %?2%) AND " +
            "(?3 IS NULL OR a.category = ?3) AND " +
            "(a.price BETWEEN ?4 AND ?5)")
    List<Annonce> searchAnnonces(String title, String description, Categorie category, double minPrice, double maxPrice);
    List<Annonce> findByVendeurId(Long vendeurId);
    List<Annonce> findByAcheteur_Id(Long acheteurId);

}


