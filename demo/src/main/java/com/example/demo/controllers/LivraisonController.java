package com.example.demo.controllers;

import com.example.demo.models.Admin;
import com.example.demo.models.Livraison;
import com.example.demo.models.Utilisateur;
import com.example.demo.services.LivraisonService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
public class LivraisonController {

    @Autowired
    private LivraisonService livraisonService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserService livreurService;

    @PostMapping("/admin/{id}/assigner-livreur/{livreurId}")
    public ResponseEntity<Livraison> assignerLivreur(@PathVariable Long id, @PathVariable Long livreurId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Admin> optionalUser = userService.findAdminByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Admin user = optionalUser.get();
        Optional<Livraison> livraison = livraisonService.assignerLivreur(id, livreurId);

        if (livraison.isPresent()) {
            return ResponseEntity.ok(livraison.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/user/api/livraisons/{id}/confirmer")
    public ResponseEntity<Livraison> confirmerLivraison(@PathVariable Long id, @RequestParam double montant) {
        Optional<Livraison> livraison = livraisonService.confirmerLivraison(id, montant);

        if (livraison.isPresent()) {
            return ResponseEntity.ok(livraison.get());
        } else {
            return ResponseEntity.notFound().build();
        }
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
