package com.example.demo.controllers;

import com.example.demo.dto.AnnonceCreateDTO;
import com.example.demo.dto.AnnonceResponseDTO;
import com.example.demo.dto.AnnonceUpdateDTO;
import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Utilisateur;
import com.example.demo.services.AnnonceService;
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
@RequestMapping("/user/api/annonces")
@CrossOrigin(origins = "http://localhost:4200")
public class AnnonceController {

    private final UserService userService;
    private final AnnonceService annonceService;

    @Autowired
    public AnnonceController(UserService userService, AnnonceService annonceService) {
        this.userService = userService;
        this.annonceService = annonceService;
    }

    @PostMapping
    public ResponseEntity<AnnonceResponseDTO> createAnnonce(@RequestBody AnnonceCreateDTO annonceDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        AnnonceResponseDTO createdAnnonce = annonceService.createAnnonce(annonceDTO, user);
        return new ResponseEntity<>(createdAnnonce, HttpStatus.CREATED);
    }

    @GetMapping("/annonces")
    public ResponseEntity<List<AnnonceResponseDTO>> getAnnocesByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        List<AnnonceResponseDTO> annonces = annonceService.findAllAnnonces(); // Assuming this should be user's annonces
        return ResponseEntity.ok(annonces);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceResponseDTO> updateAnnonce(@PathVariable Long id, @RequestBody AnnonceUpdateDTO updatedAnnonceDTO) {
        AnnonceResponseDTO updatedAnnonce = annonceService.updateAnnonce(id, updatedAnnonceDTO);
        return new ResponseEntity<>(updatedAnnonce, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        annonceService.deleteAnnonce(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<AnnonceResponseDTO>> getAllAnnonces() {
        List<AnnonceResponseDTO> annonces = annonceService.findAllAnnonces();
        return new ResponseEntity<>(annonces, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<AnnonceResponseDTO>> getAnnoncesByCategory(@PathVariable Categorie category) {
        List<AnnonceResponseDTO> annonces = annonceService.findAnnoncesByCategory(category);
        return new ResponseEntity<>(annonces, HttpStatus.OK);
    }

    @GetMapping("/disponibilite/{disponibilite}")
    public ResponseEntity<List<AnnonceResponseDTO>> getAnnoncesByDisponibilite(@PathVariable Disponibilite disponibilite) {
        List<AnnonceResponseDTO> annonces = annonceService.findAnnoncesByDisponibilite(disponibilite);
        return new ResponseEntity<>(annonces, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnnonceResponseDTO>> searchAnnonces(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Categorie category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        double minPriceValue = (minPrice != null) ? minPrice : 0.0;
        double maxPriceValue = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;

        List<AnnonceResponseDTO> annonces = annonceService.searchAnnonces(title, description, category, minPriceValue, maxPriceValue);
        return new ResponseEntity<>(annonces, HttpStatus.OK);
    }
}
