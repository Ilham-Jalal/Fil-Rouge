package com.example.demo.controllers;

import com.example.demo.models.Commentaire;
import com.example.demo.models.Utilisateur;
import com.example.demo.services.CommentaireService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/api/commentaires")
public class CommentaireController {

    @Autowired
    private CommentaireService commentaireService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Commentaire> getAllCommentaires() {
        return commentaireService.getAllCommentaires();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commentaire> getCommentaireById(@PathVariable Long id) {
        Optional<Commentaire> commentaire = commentaireService.getCommentaireById(id);
        if (commentaire.isPresent()) {
            return ResponseEntity.ok(commentaire.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Commentaire> createCommentaire(@RequestBody Commentaire commentaire) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        Commentaire savedCommentaire = commentaireService.createCommentaire(commentaire, user);
        return new ResponseEntity<>(savedCommentaire, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Commentaire> updateCommentaire(@PathVariable Long id, @RequestBody Commentaire commentaire) {
        Optional<Commentaire> updatedCommentaire = commentaireService.updateCommentaire(id, commentaire);
        return updatedCommentaire.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentaire(@PathVariable Long id) {
        if (commentaireService.getCommentaireById(id).isPresent()) {
            commentaireService.deleteCommentaire(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
