package com.example.demo.services;

import com.example.demo.models.Livraison;
import com.example.demo.repositorys.LivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;

    @Autowired
    public LivraisonService(LivraisonRepository livraisonRepository) {
        this.livraisonRepository = livraisonRepository;
    }

    public Livraison findLivraisonById(Long id) {
        Optional<Livraison> livraison = livraisonRepository.findById(id);
        if (livraison.isPresent()) {
            return livraison.get();
        } else {
            throw new RuntimeException("Livraison not found");
        }
    }

}
