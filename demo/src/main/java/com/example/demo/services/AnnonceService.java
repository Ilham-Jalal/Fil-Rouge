package com.example.demo.services;

import com.example.demo.Exeption.AnnonceNotFoundException;
import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Annonce;
import com.example.demo.models.User;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;

    @Autowired
    public AnnonceService(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    public Annonce createAnnonce(Annonce annonce, Utilisateur user) {
        annonce.setCreationDate(LocalDateTime.now());
        annonce.setUtilisateur(user);
        return annonceRepository.save(annonce);
    }
    public List<Annonce> getAnnonceByUser(Long userId) {
        return annonceRepository.findByUtilisateur_Id(userId);
    }

    public Annonce updateAnnonce(Long id, Annonce updatedAnnonce) {
        return annonceRepository.findById(id).map(annonce -> {
            annonce.setTitle(updatedAnnonce.getTitle());
            annonce.setDescription(updatedAnnonce.getDescription());
            annonce.setPrice(updatedAnnonce.getPrice());
            annonce.setCategory(updatedAnnonce.getCategory());
            annonce.setDisponibilite(updatedAnnonce.getDisponibilite());
            annonce.setLivraison(updatedAnnonce.getLivraison());
            return annonceRepository.save(annonce);
        }).orElseThrow(() -> new AnnonceNotFoundException("Annonce with ID " + id + " not found"));
    }

    public void deleteAnnonce(Long id) {
        if (!annonceRepository.existsById(id)) {
            throw new AnnonceNotFoundException("Annonce with ID " + id + " not found");
        }
        annonceRepository.deleteById(id);
    }

    public List<Annonce> findAllAnnonces() {
        return annonceRepository.findAll();
    }

    public List<Annonce> findAnnoncesByCategory(Categorie category) {
        return annonceRepository.findByCategory(category);
    }

    public List<Annonce> findAnnoncesByDisponibilite(Disponibilite disponibilite) {
        return annonceRepository.findByDisponibilite(disponibilite);
    }


    public List<Annonce> searchAnnonces(String title, String description, Categorie category, double minPrice, double maxPrice) {
        return annonceRepository.searchAnnonces(title, description, category, minPrice, maxPrice);
    }
}
