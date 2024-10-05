package com.example.demo.services;

import com.example.demo.dto.CommentaireDto;
import com.example.demo.exceptions.AccesNonAutoriseException;
import com.example.demo.models.Annonce;
import com.example.demo.models.Commentaire;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.AnnonceRepository;
import com.example.demo.repositorys.CommentaireRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final AnnonceRepository annonceRepository;

    public CommentaireService(CommentaireRepository commentaireRepository, AnnonceRepository annonceRepository) {
        this.commentaireRepository = commentaireRepository;
        this.annonceRepository = annonceRepository;
    }


    public List<CommentaireDto> getAllCommentaires() {
        return commentaireRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }


    public Optional<CommentaireDto> getCommentaireById(Long id) {
        return commentaireRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<CommentaireDto> getCommentairesByAnnonce(Long annonceId) {
        return commentaireRepository.findByAnnonceId(annonceId).stream()
                .map(this::convertToDto)
                .toList();
    }


    public CommentaireDto createCommentaire(CommentaireDto commentaireDto, Long annonceId, Utilisateur user) {
        Commentaire commentaire = convertToEntity(commentaireDto);
        commentaire.setUtilisateur(user);

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new EntityNotFoundException("Annonce not found with id: " + annonceId));
        commentaire.setAnnonce(annonce);

        return convertToDto(commentaireRepository.save(commentaire));
    }


    public Optional<CommentaireDto> updateCommentaire(Long id, CommentaireDto commentaireDto, Long currentUserId) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Commentaire not found"));

        if (!commentaire.getUtilisateur().getId().equals(currentUserId)) {
            throw new AccesNonAutoriseException("You are not authorized to update this comment");
        }

        commentaire.setContenu(commentaireDto.getContenu());

        Commentaire updatedCommentaire = commentaireRepository.save(commentaire);
        return Optional.of(new CommentaireDto(updatedCommentaire));
    }


    public void deleteCommentaire(Long id, Long currentUserId) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Commentaire not found"));

        if (!commentaire.getUtilisateur().getId().equals(currentUserId)) {
            throw new AccesNonAutoriseException("You are not authorized to delete this comment");
        }

        commentaireRepository.deleteById(id);
    }

    private CommentaireDto convertToDto(Commentaire commentaire) {
        CommentaireDto dto = new CommentaireDto();
        dto.setId(commentaire.getId());
        dto.setContenu(commentaire.getContenu());
        dto.setDateCreation(commentaire.getDateCreation());

        if (commentaire.getUtilisateur() != null) {
            dto.setUtilisateurName(commentaire.getUtilisateur().getUsername());
            dto.setUtilisateurId(commentaire.getUtilisateur().getId());
        }

        if (commentaire.getAnnonce() != null) {
            dto.setAnnonceId(commentaire.getAnnonce().getId());
        }

        return dto;
    }



    private Commentaire convertToEntity(CommentaireDto dto) {
        Commentaire commentaire = new Commentaire();
        commentaire.setId(dto.getId());
        commentaire.setContenu(dto.getContenu());
        commentaire.setDateCreation(dto.getDateCreation());
        return commentaire;
    }
}
