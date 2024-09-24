package com.example.demo.services;

import com.example.demo.Exeption.AnnonceNotFoundException;
import com.example.demo.dto.AnnonceCreateDTO;
import com.example.demo.dto.AnnonceResponseDTO;
import com.example.demo.dto.AnnonceUpdateDTO;
import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Annonce;
import com.example.demo.models.Livraison;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.AnnonceRepository;
import com.example.demo.repositorys.LivraisonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnnonceServiceTest {

    @InjectMocks
    private AnnonceService annonceService;

    @Mock
    private AnnonceRepository annonceRepository;

    @Mock
    private LivraisonRepository livraisonRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAnnonce() throws IOException {
        // Arrange
        AnnonceCreateDTO annonceCreateDTO = new AnnonceCreateDTO();
        annonceCreateDTO.setTitle("Test Title");
        annonceCreateDTO.setDescription("Test Description");
        annonceCreateDTO.setPrice(100.0);
        annonceCreateDTO.setCategory(Categorie.IMMOBILIER);
        annonceCreateDTO.setDisponibilite(Disponibilite.DISPONIBLE);

        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setUsername("TestUser");

        MultipartFile[] images = new MultipartFile[0]; // Empty array for simplicity
        when(cloudinaryService.uploadImages(images)).thenReturn(Collections.emptyList());

        Annonce annonce = new Annonce();
        annonce.setId(1L);
        annonce.setTitle(annonceCreateDTO.getTitle());
        annonce.setCreationDate(LocalDateTime.now());
        annonce.setVendeur(user);

        when(annonceRepository.save(any(Annonce.class))).thenReturn(annonce);

        // Act
        AnnonceResponseDTO responseDTO = annonceService.createAnnonce(annonceCreateDTO, user, images);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("Test Title", responseDTO.getTitle());
        verify(annonceRepository, times(1)).save(any(Annonce.class));
    }

    @Test
    void testUpdateAnnonce() {
        // Arrange
        Long annonceId = 1L;
        AnnonceUpdateDTO updateDTO = new AnnonceUpdateDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setPrice(200.0);
        updateDTO.setCategory(Categorie.VÊTEMENTS);

        Annonce existingAnnonce = new Annonce();
        existingAnnonce.setId(annonceId);
        existingAnnonce.setTitle("Old Title");

        when(annonceRepository.findById(annonceId)).thenReturn(Optional.of(existingAnnonce));
        when(annonceRepository.save(any(Annonce.class))).thenReturn(existingAnnonce);

        // Act
        AnnonceResponseDTO responseDTO = annonceService.updateAnnonce(annonceId, updateDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals("Updated Title", responseDTO.getTitle());
        verify(annonceRepository, times(1)).findById(annonceId);
        verify(annonceRepository, times(1)).save(any(Annonce.class));
    }

    @Test
    void testUpdateAnnonce_AnnonceNotFound() {
        // Arrange
        Long annonceId = 1L;
        AnnonceUpdateDTO updateDTO = new AnnonceUpdateDTO();
        when(annonceRepository.findById(annonceId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AnnonceNotFoundException.class, () -> annonceService.updateAnnonce(annonceId, updateDTO));
        verify(annonceRepository, times(1)).findById(annonceId);
    }

    @Test
    void testDeleteAnnonce() {
        // Arrange
        Long annonceId = 1L;
        when(annonceRepository.existsById(annonceId)).thenReturn(true);

        // Act
        annonceService.deleteAnnonce(annonceId);

        // Assert
        verify(annonceRepository, times(1)).deleteById(annonceId);
    }

    @Test
    void testDeleteAnnonce_AnnonceNotFound() {
        // Arrange
        Long annonceId = 1L;
        when(annonceRepository.existsById(annonceId)).thenReturn(false);

        // Act & Assert
        assertThrows(AnnonceNotFoundException.class, () -> annonceService.deleteAnnonce(annonceId));
        verify(annonceRepository, times(1)).existsById(annonceId);
    }

    @Test
    void testFindAllAnnonces() {
        // Arrange
        Annonce annonce = new Annonce();
        annonce.setId(1L);
        annonce.setTitle("Test Annonce");

        when(annonceRepository.findAll()).thenReturn(Collections.singletonList(annonce));

        // Act
        var annonces = annonceService.findAllAnnonces();

        // Assert
        assertEquals(1, annonces.size());
        assertEquals("Test Annonce", annonces.get(0).getTitle());
        verify(annonceRepository, times(1)).findAll();
    }
}
