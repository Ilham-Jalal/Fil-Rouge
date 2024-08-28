package com.example.demo.services;

import com.example.demo.Exeption.AnnonceNotFoundException;
import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Annonce;
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

    public Annonce createAnnonce(Annonce annonce) {
        annonce.setCreationDate(LocalDateTime.now());
        return annonceRepository.save(annonce);
    }

    public Annonce updateAnnonce(Long id, Annonce updatedAnnonce) {
        return annonceRepository.findById(id).map(annonce -> {
            annonce.setTitle(updatedAnnonce.getTitle());
            annonce.setDescription(updatedAnnonce.getDescription());
            annonce.setPrice(updatedAnnonce.getPrice());
            annonce.setCategory(updatedAnnonce.getCategory());
            annonce.setDisponibilite(updatedAnnonce.getDisponibilite());
            annonce.setLivraison(updatedAnnonce.getLivraison());
            annonce.setUtilisateur(updatedAnnonce.getUtilisateur());
            return annonceRepository.save(annonce);
        }).orElseThrow(() -> new AnnonceNotFoundException("Annonce with ID " + id + " not found"));
    }


    public void deleteAnnonce(Long id) {
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
}
