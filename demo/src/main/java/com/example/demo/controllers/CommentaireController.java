package com.example.demo.controllers;

import com.example.demo.dto.CommentaireDto;
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
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user/api/commentaires")
public class CommentaireController {

    @Autowired
    private CommentaireService commentaireService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<CommentaireDto> getAllCommentaires() {
        return commentaireService.getAllCommentaires();
    }
    @GetMapping("/annonce/{annonceId}")
    public List<CommentaireDto> getCommentairesByAnnonce(@PathVariable Long annonceId) {
        return commentaireService.getCommentairesByAnnonce(annonceId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CommentaireDto> getCommentaireById(@PathVariable Long id) {
        Optional<CommentaireDto> commentaireDto = commentaireService.getCommentaireById(id);
        if (commentaireDto.isPresent()) {
            return ResponseEntity.ok(commentaireDto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{annonceId}")
    public ResponseEntity<CommentaireDto> createCommentaire(
            @RequestBody CommentaireDto commentaireDto,
            @PathVariable Long annonceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        CommentaireDto savedCommentaireDto = commentaireService.createCommentaire(commentaireDto, annonceId, user);
        return new ResponseEntity<>(savedCommentaireDto, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CommentaireDto> updateCommentaire(@PathVariable Long id, @RequestBody CommentaireDto commentaireDto) {
        Optional<CommentaireDto> updatedCommentaireDto = commentaireService.updateCommentaire(id, commentaireDto);
        return updatedCommentaireDto.map(ResponseEntity::ok)
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
