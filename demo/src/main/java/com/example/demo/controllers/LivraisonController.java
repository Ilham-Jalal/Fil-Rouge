package com.example.demo.controllers;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.models.*;
import com.example.demo.services.AnnonceService;
import com.example.demo.services.LivraisonService;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LivraisonController {


    private final LivraisonService livraisonService;

    private final UserService userService;

    private final AnnonceService annonceService;

    public LivraisonController(LivraisonService livraisonService, UserService userService, AnnonceService annonceService) {
        this.livraisonService = livraisonService;
        this.userService = userService;
        this.annonceService = annonceService;
    }

    @PostMapping("/admin/{id}/assigner-livreur/{livreurId}")
    public ResponseEntity<Livraison> assignerLivreur(@PathVariable Long id, @PathVariable Long livreurId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Admin> optionalUser = userService.findAdminByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Livraison> livraison = livraisonService.assignerLivreur(id, livreurId);

        return livraison.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user/api/livraisons/{annonceId}/associer-livraison/{livraisonId}")
    public ResponseEntity<Annonce> associerLivraison(@PathVariable Long annonceId, @PathVariable Long livraisonId) {
        Optional<Annonce> annonce = annonceService.assignerLivraison(annonceId, livraisonId);

        if (annonce.isPresent()) {
            return ResponseEntity.ok(annonce.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/livreur/confirmer/{id}")
    public ResponseEntity<Livraison> confirmerLivraison(@PathVariable Long id, @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(livraisonService.confirmerLivraison(id, paymentRequest.getMontant()));
    }

    @PostMapping("/user/api/livraisons")
    public ResponseEntity<Livraison> createLivraison(@RequestBody Livraison livraison) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        livraison.setUtilisateur(user);
        return ResponseEntity.ok(livraisonService.createLivraison(livraison));
    }

    @DeleteMapping("/user/api/livraisons/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        if (livraisonService.getLivraisonById(id).isPresent()) {
            livraisonService.deleteLivraison(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
