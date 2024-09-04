package com.example.demo.services;

import com.example.demo.dto.CommentaireDto;
import com.example.demo.models.Annonce;
import com.example.demo.models.Commentaire;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.AnnonceRepository;
import com.example.demo.repositorys.CommentaireRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentaireService {

    @Autowired
    private CommentaireRepository commentaireRepository;
    @Autowired
    private AnnonceRepository annonceRepository;


    public List<CommentaireDto> getAllCommentaires() {
        return commentaireRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public Optional<CommentaireDto> getCommentaireById(Long id) {
        return commentaireRepository.findById(id)
                .map(this::convertToDto);
    }

    public CommentaireDto createCommentaire(CommentaireDto commentaireDto, Long annonceId, Utilisateur user) {
        Commentaire commentaire = convertToEntity(commentaireDto);
        commentaire.setUtilisateur(user);

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new EntityNotFoundException("Annonce not found with id: " + annonceId));
        commentaire.setAnnonce(annonce);

        return convertToDto(commentaireRepository.save(commentaire));
    }

    public Optional<CommentaireDto> updateCommentaire(Long id, CommentaireDto updatedCommentaireDto) {
        return commentaireRepository.findById(id).map(existingCommentaire -> {
            existingCommentaire.setContenu(updatedCommentaireDto.getContenu());
            return convertToDto(commentaireRepository.save(existingCommentaire));
        });
    }

    public void deleteCommentaire(Long id) {
        commentaireRepository.deleteById(id);
    }

    private CommentaireDto convertToDto(Commentaire commentaire) {
        CommentaireDto dto = new CommentaireDto();
        dto.setId(commentaire.getId());
        dto.setContenu(commentaire.getContenu());
        dto.setDateCreation(commentaire.getDateCreation());

        if (commentaire.getUtilisateur() != null) {
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
        // utilisateur and annonce will be set in the service
        return commentaire;
    }
}
