package com.example.demo.services;

import com.example.demo.enums.StatutLivraison;
import com.example.demo.models.Livraison;
import com.example.demo.models.Livreur;
import com.example.demo.models.Transaction;
import com.example.demo.repositorys.LivraisonRepository;
import com.example.demo.repositorys.LivreurRepository;
import com.example.demo.repositorys.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class LivraisonService {

    @Autowired
    private LivraisonRepository livraisonRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LivreurRepository livreurRepository;

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

    public Optional<Livraison> confirmerLivraison(Long livraisonId, double montant) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);

        if (livraisonOpt.isPresent()) {
            Livraison livraison = livraisonOpt.get();
            livraison.setStatut(StatutLivraison.LIVRÉ);
            livraison.setDateLivraison(LocalDateTime.now());
            livraisonRepository.save(livraison);

            Transaction transaction = new Transaction();
            transaction.setDateTransaction(LocalDateTime.now());
            transaction.setMontant(montant);
            transaction.setVendeur(livraison.getUtilisateur());
            transaction.setAcheteur(livraison.getAnnonces().get(0).getUtilisateur());
            transaction.setAnnonce(livraison.getAnnonces().get(0));
            transactionRepository.save(transaction);

            return Optional.of(livraison);
        }

        return Optional.empty();
    }

    public Livraison createLivraison(Livraison livraison) {
        return livraisonRepository.save(livraison);
    }

    public void deleteLivraison(Long id) {
        livraisonRepository.deleteById(id);
    }
}
