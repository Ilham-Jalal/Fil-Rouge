package com.example.demo.services;

import com.example.demo.dto.AnnonceCreateDTO;
import com.example.demo.dto.AnnonceResponseDTO;
import com.example.demo.dto.AnnonceUpdateDTO;
import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.exceptions.AnnonceNotFoundException;
import com.example.demo.models.Annonce;
import com.example.demo.models.Livraison;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.AnnonceRepository;
import com.example.demo.repositorys.LivraisonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnnonceServiceTest {

    @Mock
    private AnnonceRepository annonceRepository;

    @Mock
    private LivraisonRepository livraisonRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private AnnonceService annonceService;

    private Utilisateur testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new Utilisateur();
        testUser.setId(1L);
        testUser.setUsername("testUser");
    }

    @Test
    void testCreateAnnonce() {

        AnnonceCreateDTO annonceDTO = new AnnonceCreateDTO();
        annonceDTO.setTitle("Test Annonce");
        annonceDTO.setDescription("Test Description");
        annonceDTO.setPrice(100.0);
        annonceDTO.setCategory(Categorie.ELECTRONIQUE);
        annonceDTO.setDisponibilite(Disponibilite.DISPONIBLE);

        MultipartFile[] images = new MultipartFile[0];
        when(cloudinaryService.uploadImages(images)).thenReturn(Collections.singletonList("http://image.url"));

        Annonce savedAnnonce = new Annonce();
        savedAnnonce.setId(1L);
        savedAnnonce.setTitle(annonceDTO.getTitle());
        savedAnnonce.setDescription(annonceDTO.getDescription());
        savedAnnonce.setPrice(annonceDTO.getPrice());
        savedAnnonce.setCategory(annonceDTO.getCategory());
        savedAnnonce.setDisponibilite(annonceDTO.getDisponibilite());
        savedAnnonce.setCreationDate(LocalDateTime.now());
        savedAnnonce.setVendeur(testUser);
        savedAnnonce.setImages(Collections.singletonList("http://image.url"));

        when(annonceRepository.save(any(Annonce.class))).thenReturn(savedAnnonce);

        AnnonceResponseDTO responseDTO = annonceService.createAnnonce(annonceDTO, testUser, images);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Test Annonce", responseDTO.getTitle());
        verify(annonceRepository, times(1)).save(any(Annonce.class));
    }

    @Test
    void testFindById() {
        // Given
        Annonce annonce = new Annonce();
        annonce.setId(1L);
        annonce.setTitle("Test Annonce");
        when(annonceRepository.findById(1L)).thenReturn(Optional.of(annonce));

        // When
        AnnonceResponseDTO responseDTO = annonceService.findById(1L);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Test Annonce", responseDTO.getTitle());
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(annonceRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AnnonceNotFoundException.class, () -> annonceService.findById(1L));
    }

    @Test
    void testUpdateAnnonce() {
        // Given
        Long annonceId = 1L;
        AnnonceUpdateDTO updateDTO = new AnnonceUpdateDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setPrice(150.0);
        updateDTO.setCategory(Categorie.ELECTRONIQUE);
        updateDTO.setDisponibilite(Disponibilite.DISPONIBLE);

        Annonce existingAnnonce = new Annonce();
        existingAnnonce.setId(annonceId);
        existingAnnonce.setTitle("Old Title");
        when(annonceRepository.findById(annonceId)).thenReturn(Optional.of(existingAnnonce));
        when(annonceRepository.save(existingAnnonce)).thenReturn(existingAnnonce);

        // When
        AnnonceResponseDTO responseDTO = annonceService.updateAnnonce(annonceId, updateDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Updated Title", responseDTO.getTitle());
        verify(annonceRepository, times(1)).save(existingAnnonce);
    }

    @Test
    void testDeleteAnnonce() {
        // Given
        Long annonceId = 1L;
        when(annonceRepository.existsById(annonceId)).thenReturn(true);

        // When
        annonceService.deleteAnnonce(annonceId);

        // Then
        verify(annonceRepository, times(1)).deleteById(annonceId);
    }

    @Test
    void testDeleteAnnonce_NotFound() {
        // Given
        Long annonceId = 1L;
        when(annonceRepository.existsById(annonceId)).thenReturn(false);

        // When & Then
        assertThrows(AnnonceNotFoundException.class, () -> annonceService.deleteAnnonce(annonceId));
    }

    @Test
    void testFindAllAnnonces() {
        // Given
        Annonce annonce = new Annonce();
        annonce.setId(1L);
        annonce.setTitle("Test Annonce");
        when(annonceRepository.findAll()).thenReturn(Collections.singletonList(annonce));

        // When
        List<AnnonceResponseDTO> annonces = annonceService.findAllAnnonces();

        // Then
        assertEquals(1, annonces.size());
        assertEquals("Test Annonce", annonces.get(0).getTitle());
    }

    @Test
    void testCountTotalAnnonces() {
        // Given
        when(annonceRepository.count()).thenReturn(10L);

        // When
        long count = annonceService.countTotalAnnonces();

        // Then
        assertEquals(10L, count);
    }
}
