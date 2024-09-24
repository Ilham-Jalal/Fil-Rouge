package com.example.demo.services;

import com.example.demo.enums.StatutLivraison;
import com.example.demo.models.Livraison;
import com.example.demo.models.Livreur;
import com.example.demo.repositorys.AnnonceRepository;
import com.example.demo.repositorys.LivraisonRepository;
import com.example.demo.repositorys.LivreurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivraisonServiceTest {

    @Mock
    private LivraisonRepository livraisonRepository;

    @Mock
    private AnnonceRepository annonceRepository;

    @Mock
    private LivreurRepository livreurRepository;

    @InjectMocks
    private LivraisonService livraisonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLivraisonById() {
        // Given
        Livraison livraison = new Livraison();
        livraison.setId(1L);
        when(livraisonRepository.findById(1L)).thenReturn(Optional.of(livraison));

        // When
        Optional<Livraison> result = livraisonService.getLivraisonById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testAssignerLivreur() {
        // Given
        Livraison livraison = new Livraison();
        livraison.setId(1L);
        Livreur livreur = new Livreur();
        livreur.setId(1L);

        when(livraisonRepository.findById(1L)).thenReturn(Optional.of(livraison));
        when(livreurRepository.findById(1L)).thenReturn(Optional.of(livreur));
        when(livraisonRepository.save(livraison)).thenReturn(livraison);

        // When
        Optional<Livraison> result = livraisonService.assignerLivreur(1L, 1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(livreur, result.get().getLivreur());
    }

    @Test
    void testAssignerLivreurNonExistant() {
        // Given
        when(livraisonRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Livraison> result = livraisonService.assignerLivreur(1L, 1L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testConfirmerLivraison() {
        // Given
        Livraison livraison = new Livraison();
        livraison.setId(1L);
        livraison.setStatut(StatutLivraison.EN_COURS);

        when(livraisonRepository.findById(1L)).thenReturn(Optional.of(livraison));
        when(livraisonRepository.save(any(Livraison.class))).thenReturn(livraison);

        // When
        Livraison result = livraisonService.confirmerLivraison(1L, 100.0);

        // Then
        assertEquals(StatutLivraison.LIVRÉ, result.getStatut());
        assertNotNull(result.getDateLivraison());
        assertEquals(100.0, result.getMontant());
    }

    @Test
    void testConfirmerLivraisonNonTrouvee() {
        // Given
        when(livraisonRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> {
            livraisonService.confirmerLivraison(1L, 100.0);
        });
    }

    @Test
    void testCreateLivraison() {
        // Given
        Livraison livraison = new Livraison();
        when(livraisonRepository.save(livraison)).thenReturn(livraison);

        // When
        Livraison result = livraisonService.createLivraison(livraison);

        // Then
        assertNotNull(result);
        verify(livraisonRepository, times(1)).save(livraison);
    }

    @Test
    void testDeleteLivraison() {
        // Given
        Long livraisonId = 1L;

        // When
        livraisonService.deleteLivraison(livraisonId);

        // Then
        verify(livraisonRepository, times(1)).deleteById(livraisonId);
    }
}
