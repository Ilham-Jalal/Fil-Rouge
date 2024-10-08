package com.example.demo.services;

import com.example.demo.dto.CommentaireDto;
import com.example.demo.exceptions.AccesNonAutoriseException;
import com.example.demo.models.Annonce;
import com.example.demo.models.Commentaire;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.AnnonceRepository;
import com.example.demo.repositorys.CommentaireRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentaireServiceTest {

    @Mock
    private CommentaireRepository commentaireRepository;

    @Mock
    private AnnonceRepository annonceRepository;

    @InjectMocks
    private CommentaireService commentaireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCommentaires() {
        // Given
        Commentaire commentaire = new Commentaire();
        commentaire.setId(1L);
        when(commentaireRepository.findAll()).thenReturn(Arrays.asList(commentaire));

        // When
        List<CommentaireDto> result = commentaireService.getAllCommentaires();

        // Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testGetCommentaireById() {
        // Given
        Commentaire commentaire = new Commentaire();
        commentaire.setId(1L);
        when(commentaireRepository.findById(1L)).thenReturn(Optional.of(commentaire));

        // When
        Optional<CommentaireDto> result = commentaireService.getCommentaireById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetCommentairesByAnnonce() {
        // Given
        Commentaire commentaire = new Commentaire();
        commentaire.setId(1L);
        when(commentaireRepository.findByAnnonceId(1L)).thenReturn(Arrays.asList(commentaire));

        // When
        List<CommentaireDto> result = commentaireService.getCommentairesByAnnonce(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testCreateCommentaire() {
        // Given
        CommentaireDto commentaireDto = new CommentaireDto();
        commentaireDto.setContenu("Test comment");
        Utilisateur user = new Utilisateur();
        user.setUsername("testUser");
        Annonce annonce = new Annonce();
        annonce.setId(1L);
        Commentaire commentaire = new Commentaire();
        commentaire.setId(1L);
        when(annonceRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(commentaireRepository.save(any(Commentaire.class))).thenReturn(commentaire);

        // When
        CommentaireDto result = commentaireService.createCommentaire(commentaireDto, 1L, user);

        // Then
        assertNotNull(result);
    }

    @Test
    void testCreateCommentaireAnnonceNotFound() {
        // Given
        CommentaireDto commentaireDto = new CommentaireDto();
        Utilisateur user = new Utilisateur();
        when(annonceRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> {
            commentaireService.createCommentaire(commentaireDto, 1L, user);
        });
    }

    @Test
    void testUpdateCommentaire() {
        // Given
        Commentaire commentaire = new Commentaire();
        commentaire.setId(1L);
        commentaire.setContenu("Old content");

        // Create a mock Utilisateur and set it to the commentaire
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L); // The owner of the comment
        commentaire.setUtilisateur(utilisateur);

        CommentaireDto updatedDto = new CommentaireDto();
        updatedDto.setContenu("Updated content");

        // Stubbing the repository methods
        when(commentaireRepository.findById(1L)).thenReturn(Optional.of(commentaire));
        when(commentaireRepository.save(any(Commentaire.class))).thenReturn(commentaire);

        // When
        Optional<CommentaireDto> result = commentaireService.updateCommentaire(1L, updatedDto, 1L); // Passing the owner ID

        // Then
        assertTrue(result.isPresent());
        assertEquals("Updated content", result.get().getContenu());
        assertEquals(1L, result.get().getId()); // Check that the ID is still the same
        verify(commentaireRepository).save(commentaire); // Ensure save was called
    }


    @Test
    void testDeleteCommentaire() {
        // Given
        Long id = 1L;
        Long currentUserId = 1L; // Assuming the user with ID 1 is the owner

        // Create a Commentaire and set the owner
        Commentaire commentaire = new Commentaire();
        commentaire.setId(id);
        Utilisateur owner = new Utilisateur();
        owner.setId(currentUserId);
        commentaire.setUtilisateur(owner); // Set the owner

        // Stubbing the repository method to return the comment
        when(commentaireRepository.findById(id)).thenReturn(Optional.of(commentaire));

        // When
        commentaireService.deleteCommentaire(id, currentUserId); // Pass the current user's ID

        // Then
        verify(commentaireRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCommentaire_Unauthorized() {
        // Given
        Long id = 1L;
        Long unauthorizedUserId = 2L; // User trying to delete the comment is not the owner

        // Create a Commentaire and set a different owner
        Commentaire commentaire = new Commentaire();
        commentaire.setId(id);
        Utilisateur owner = new Utilisateur();
        owner.setId(1L); // The owner has ID 1
        commentaire.setUtilisateur(owner); // Set the owner

        // Stubbing the repository method to return the comment
        when(commentaireRepository.findById(id)).thenReturn(Optional.of(commentaire));

        // When / Then
        assertThrows(AccesNonAutoriseException.class, () -> {
            commentaireService.deleteCommentaire(id, unauthorizedUserId); // Attempt to delete with unauthorized user ID
        });

        // Verify deleteById is never called
        verify(commentaireRepository, never()).deleteById(id);
    }

}
