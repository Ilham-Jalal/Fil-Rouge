package com.example.demo.controllers;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.models.*;
import com.example.demo.services.AnnonceService;
import com.example.demo.services.LivraisonService;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @GetMapping("/admin/livraisons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Livraison>> getAllLivraisons() {
        List<Livraison> livraisons = livraisonService.getAllLivraisons();
        return ResponseEntity.ok(livraisons);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('LIVREUR')")
    @GetMapping("/livraisons/{id}/details")
    public ResponseEntity<Livraison> getLivraisonDetails(@PathVariable Long id) {
        Optional<Livraison> livraisonOpt = livraisonService.getLivraisonDetails(id);

        return livraisonOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/livraisons")
    public ResponseEntity<List<Livraison>> getUserLivraisons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        List<Livraison> livraisons = livraisonService.getLivraisonsByUser(user);
        return ResponseEntity.ok(livraisons);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIVREUR')")
    @GetMapping("/livraison")
    public ResponseEntity<List<Livraison>> getLivreurLivraisons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Livreur> optionalUser = userService.findLivreurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Livreur livreur = optionalUser.get();
        List<Livraison> livraisons = livraisonService.getLivraisonsByLivreur(livreur);
        return ResponseEntity.ok(livraisons);
    }
    @PreAuthorize("hasRole('LIVREUR') or hasRole('USER')")
    @PutMapping("/livraisons/{id}")
    public ResponseEntity<Livraison> updateUserLivraison(@PathVariable Long id, @RequestBody Livraison updatedLivraison) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        Optional<Livraison> livraison = livraisonService.updateLivraisonByUser(id, updatedLivraison, user);

        return livraison.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/livraisons/{id}")
    public ResponseEntity<Void> deleteUserLivraison(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        boolean isDeleted = livraisonService.deleteLivraisonByUser(id, user);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
