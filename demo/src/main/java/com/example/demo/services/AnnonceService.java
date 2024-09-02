package com.example.demo.services;

import com.example.demo.Exeption.AnnonceNotFoundException;
import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Annonce;
import com.example.demo.models.Livraison;
import com.example.demo.models.User;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.AnnonceRepository;
import com.example.demo.repositorys.LivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;

    private final LivraisonRepository livraisonRepository;

    @Autowired
    public AnnonceService(AnnonceRepository annonceRepository, LivraisonRepository livraisonRepository) {
        this.annonceRepository = annonceRepository;
        this.livraisonRepository = livraisonRepository;
    }

    public Annonce createAnnonce(Annonce annonce, Utilisateur user) {
        annonce.setCreationDate(LocalDateTime.now());
        return annonceRepository.save(annonce);
    }
    public List<Annonce> getAnnonceByUser(Long userId) {
        return annonceRepository.findByVendeurId(userId);
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

    public Optional<Annonce> assignerLivraison(Long annonceId, Long livraisonId) {
        Optional<Annonce> annonceOpt = annonceRepository.findById(annonceId);
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);

        if (annonceOpt.isPresent() && livraisonOpt.isPresent()) {
            Annonce annonce = annonceOpt.get();
            Livraison livraison = livraisonOpt.get();
            annonce.setLivraison(livraison);
            annonceRepository.save(annonce);
            return Optional.of(annonce);
        }

        return Optional.empty();
    }

    public List<Annonce> searchAnnonces(String title, String description, Categorie category, double minPrice, double maxPrice) {
        return annonceRepository.searchAnnonces(title, description, category, minPrice, maxPrice);
    }
}
