package com.example.demo.services;

import com.example.demo.enums.StatutLivraison;
import com.example.demo.models.Livraison;
import com.example.demo.models.Livreur;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.LivraisonRepository;
import com.example.demo.repositorys.LivreurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivraisonServiceTest {

    @Mock
    private LivraisonRepository livraisonRepository;

    @Mock
    private LivreurRepository livreurRepository;

    @InjectMocks
    private LivraisonService livraisonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLivraison() {
        Livraison livraison = new Livraison();
        livraison.setAdresseAcheteur("Acheteur 1");

        when(livraisonRepository.save(livraison)).thenReturn(livraison);

        Livraison result = livraisonService.createLivraison(livraison);

        assertNotNull(result);
        assertEquals("Acheteur 1", result.getAdresseAcheteur());
        verify(livraisonRepository, times(1)).save(livraison);
    }

    @Test
    void testAssignerLivreur() {
        Livraison livraison = new Livraison();
        Livreur livreur = new Livreur();
        livreur.setId(1L);

        when(livraisonRepository.findById(1L)).thenReturn(Optional.of(livraison));
        when(livreurRepository.findById(1L)).thenReturn(Optional.of(livreur));

        Optional<Livraison> result = livraisonService.assignerLivreur(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(livreur, result.get().getLivreur());
        verify(livraisonRepository, times(1)).save(livraison);
    }

    @Test
    void testConfirmerLivraison_EnCours() {
        Livraison livraison = new Livraison();
        livraison.setStatut(StatutLivraison.EN_COURS);

        when(livraisonRepository.findById(1L)).thenReturn(Optional.of(livraison));

        Livraison result = livraisonService.confirmerLivraison(1L, 100.0);

        assertNotNull(result);
        assertEquals(StatutLivraison.LIVRE, result.getStatut());
        assertEquals(100.0, result.getMontant());
        assertNotNull(result.getDateLivraison());
        verify(livraisonRepository, times(1)).save(livraison);
    }

    @Test
    void testConfirmerLivraison_NotEnCours() {
        Livraison livraison = new Livraison();
        livraison.setStatut(StatutLivraison.LIVRE);

        when(livraisonRepository.findById(1L)).thenReturn(Optional.of(livraison));

        assertThrows(IllegalStateException.class, () -> livraisonService.confirmerLivraison(1L, 100.0));
        verify(livraisonRepository, times(0)).save(livraison);
    }

    @Test
    void testDeleteLivraisonByUser_Authorized() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        Livraison livraison = new Livraison();
        livraison.setUtilisateur(utilisateur);

        when(livraisonRepository.findById(1L)).thenReturn(Optional.of(livraison));

        boolean result = livraisonService.deleteLivraisonByUser(1L, utilisateur);

        assertTrue(result);
        verify(livraisonRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLivraisonByUser_Unauthorized() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        Livraison livraison = new Livraison();
        Utilisateur autreUtilisateur = new Utilisateur();
        autreUtilisateur.setId(2L);
        livraison.setUtilisateur(autreUtilisateur);

        when(livraisonRepository.findById(1L)).thenReturn(Optional.of(livraison));

        assertThrows(IllegalStateException.class, () -> livraisonService.deleteLivraisonByUser(1L, utilisateur));
        verify(livraisonRepository, times(0)).deleteById(1L);
    }
}
