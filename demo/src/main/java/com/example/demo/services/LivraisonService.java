package com.example.demo.services;

import com.example.demo.enums.StatutLivraison;
import com.example.demo.models.Livraison;
import com.example.demo.models.Livreur;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.LivraisonRepository;
import com.example.demo.repositorys.LivreurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;

    private final LivreurRepository livreurRepository;

    public LivraisonService(LivraisonRepository livraisonRepository, LivreurRepository livreurRepository) {
        this.livraisonRepository = livraisonRepository;
        this.livreurRepository = livreurRepository;
    }

    public Optional<Livraison> getLivraisonById(Long id) {
        return livraisonRepository.findById(id);
    }

    public Optional<Livraison> assignerLivreur(Long livraisonId, Long livreurId) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);
        Optional<Livreur> livreurOpt = livreurRepository.findById(livreurId);

        if (livraisonOpt.isPresent() && livreurOpt.isPresent()) {
            Livraison livraison = livraisonOpt.get();
            Livreur livreur = livreurOpt.get();
            livraison.setLivreur(livreur);
            livraisonRepository.save(livraison);
            return Optional.of(livraison);
        }

        return Optional.empty();
    }


    public Livraison confirmerLivraison(Long livraisonId, double montant) {
        Livraison livraison = livraisonRepository.findById(livraisonId)
                .orElseThrow(() -> new EntityNotFoundException("Livraison not found"));

        if (!livraison.getStatut().equals(StatutLivraison.EN_COURS)) {
            throw new IllegalStateException("Cannot confirm delivery: Livraison is not in progress."); // Handle the case appropriately
        }

        livraison.setStatut(StatutLivraison.LIVRE);
        livraison.setDateLivraison(LocalDateTime.now());
        livraison.setMontant(montant);
        livraisonRepository.save(livraison);

        return livraison;
    }

    public List<Livraison> getLivraisonsByUser(Utilisateur utilisateur) {
        return livraisonRepository.findByUtilisateur(utilisateur);
    }

    public Optional<Livraison> updateLivraisonByUser(Long livraisonId, Livraison updatedLivraison, Utilisateur utilisateur) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);

        if (livraisonOpt.isPresent()) {
            Livraison livraison = livraisonOpt.get();
            if (!livraison.getUtilisateur().equals(utilisateur)) {
                throw new IllegalStateException("Unauthorized to update this delivery");
            }

            livraison.setAdresseVendeur(updatedLivraison.getAdresseVendeur());
            livraison.setAdresseAcheteur(updatedLivraison.getAdresseAcheteur());
            livraison.setMontant(updatedLivraison.getMontant());
            livraison.setStatut(updatedLivraison.getStatut());

            return Optional.of(livraisonRepository.save(livraison));
        }

        return Optional.empty();
    }

    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }
    public boolean deleteLivraisonByUser(Long livraisonId, Utilisateur utilisateur) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);

        if (livraisonOpt.isPresent()) {
            Livraison livraison = livraisonOpt.get();
            if (!livraison.getUtilisateur().equals(utilisateur)) {
                throw new IllegalStateException("Unauthorized to delete this delivery");
            }

            livraisonRepository.deleteById(livraisonId);
            return true;
        }

        return false;
    }
    public Livraison createLivraison(Livraison livraison) {
        return livraisonRepository.save(livraison);
    }

    public void deleteLivraison(Long id) {
        livraisonRepository.deleteById(id);
    }
}
