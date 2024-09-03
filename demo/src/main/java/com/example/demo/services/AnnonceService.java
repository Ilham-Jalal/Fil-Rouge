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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final LivraisonRepository livraisonRepository;

    @Autowired
    public AnnonceService(AnnonceRepository annonceRepository, LivraisonRepository livraisonRepository) {
        this.annonceRepository = annonceRepository;
        this.livraisonRepository = livraisonRepository;
    }

    public AnnonceResponseDTO createAnnonce(AnnonceCreateDTO annonceDTO, Utilisateur user) {
        Annonce annonce = new Annonce();
        annonce.setTitle(annonceDTO.getTitle());
        annonce.setDescription(annonceDTO.getDescription());
        annonce.setPrice(annonceDTO.getPrice());
        annonce.setCategory(annonceDTO.getCategory());
        annonce.setDisponibilite(annonceDTO.getDisponibilite());
        annonce.setCreationDate(LocalDateTime.now());
        annonce.setVendeur(user);
        annonce = annonceRepository.save(annonce);
        return mapToResponseDTO(annonce);
    }

    public AnnonceResponseDTO updateAnnonce(Long id, AnnonceUpdateDTO updatedAnnonceDTO) {
        return annonceRepository.findById(id).map(annonce -> {
            annonce.setTitle(updatedAnnonceDTO.getTitle());
            annonce.setDescription(updatedAnnonceDTO.getDescription());
            annonce.setPrice(updatedAnnonceDTO.getPrice());
            annonce.setCategory(updatedAnnonceDTO.getCategory());
            annonce.setDisponibilite(updatedAnnonceDTO.getDisponibilite());

            if (updatedAnnonceDTO.getLivraisonId() != null) {
                Optional<Livraison> livraisonOpt = livraisonRepository.findById(updatedAnnonceDTO.getLivraisonId());
                livraisonOpt.ifPresent(annonce::setLivraison);
            }

            annonce = annonceRepository.save(annonce);
            return mapToResponseDTO(annonce);
        }).orElseThrow(() -> new AnnonceNotFoundException("Annonce with ID " + id + " not found"));
    }

    public void deleteAnnonce(Long id) {
        if (!annonceRepository.existsById(id)) {
            throw new AnnonceNotFoundException("Annonce with ID " + id + " not found");
        }
        annonceRepository.deleteById(id);
    }

    public List<AnnonceResponseDTO> findAllAnnonces() {
        return annonceRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceResponseDTO> findAnnoncesByCategory(Categorie category) {
        return annonceRepository.findByCategory(category).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceResponseDTO> findAnnoncesByDisponibilite(Disponibilite disponibilite) {
        return annonceRepository.findByDisponibilite(disponibilite).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceResponseDTO> searchAnnonces(String title, String description, Categorie category, double minPrice, double maxPrice) {
        return annonceRepository.searchAnnonces(title, description, category, minPrice, maxPrice).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private AnnonceResponseDTO mapToResponseDTO(Annonce annonce) {
        AnnonceResponseDTO dto = new AnnonceResponseDTO();
        dto.setId(annonce.getId());
        dto.setTitle(annonce.getTitle());
        dto.setDescription(annonce.getDescription());
        dto.setPrice(annonce.getPrice());
        dto.setCategory(annonce.getCategory());
        dto.setDisponibilite(annonce.getDisponibilite());
        dto.setCreationDate(annonce.getCreationDate());
        dto.setVendeurId(annonce.getVendeur().getId());
        dto.setAcheteurId(annonce.getAcheteur() != null ? annonce.getAcheteur().getId() : null);
        dto.setLivraisonId(annonce.getLivraison() != null ? annonce.getLivraison().getId() : null);
        return dto;
    }
}
