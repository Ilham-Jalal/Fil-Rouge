package com.example.demo.services;

import com.example.demo.models.Commentaire;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.CommentaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentaireService {

    @Autowired
    private CommentaireRepository commentaireRepository;

    public List<Commentaire> getAllCommentaires() {
        return commentaireRepository.findAll();
    }

    public Optional<Commentaire> getCommentaireById(Long id) {
        return commentaireRepository.findById(id);
    }

    public Commentaire createCommentaire(Commentaire commentaire, Utilisateur user) {
        commentaire.setUtilisateur(user);
        return commentaireRepository.save(commentaire);
    }

    public Optional<Commentaire> updateCommentaire(Long id, Commentaire updatedCommentaire) {
        return commentaireRepository.findById(id).map(existingCommentaire -> {
            existingCommentaire.setContenu(updatedCommentaire.getContenu());
            return commentaireRepository.save(existingCommentaire);
        });
    }

    public void deleteCommentaire(Long id) {
        commentaireRepository.deleteById(id);
    }
}
