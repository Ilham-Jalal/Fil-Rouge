package com.example.demo.controllers;

import com.example.demo.Exeption.AnnonceNotFoundException;
import com.example.demo.Exeption.UserNotFoundExeption;
import com.example.demo.Exeption.UtilisateurNonTrouveException;
import com.example.demo.dto.AnnonceCreateDTO;
import com.example.demo.dto.AnnonceResponseDTO;
import com.example.demo.dto.AnnonceUpdateDTO;
import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Utilisateur;
import com.example.demo.services.AnnonceService;
import com.example.demo.services.CloudinaryService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/api/annonces")
@CrossOrigin(origins = "http://localhost:4200")
public class AnnonceController {

    private final UserService userService;
    private final AnnonceService annonceService;

    @Autowired
    public AnnonceController(UserService userService, AnnonceService annonceService, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.annonceService = annonceService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnnonceResponseDTO> createAnnonce(
            @RequestPart("annonce") AnnonceCreateDTO annonceDTO,
            @RequestPart("images") MultipartFile[] images) {

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Utilisateur user = userService.findUtilisateurByUsername(username)
                    .orElseThrow(() -> new UserNotFoundExeption("Utilisateur non trouvé avec le nom d'utilisateur : " + username));

            // Appeler le service pour créer l'annonce
            AnnonceResponseDTO response = annonceService.createAnnonce(annonceDTO, user, images);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotFoundExeption e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/annonces")
    public ResponseEntity<List<AnnonceResponseDTO>> getAnnoncesByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UtilisateurNonTrouveException("Utilisateur non trouvé pour le nom d'utilisateur: " + username);
        }

        Utilisateur user = optionalUser.get();
        List<AnnonceResponseDTO> annonces = annonceService.findAnnoncesByUser(user.getId());
        return ResponseEntity.ok(annonces);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AnnonceResponseDTO> updateAnnonce(@PathVariable Long id, @RequestBody AnnonceUpdateDTO updatedAnnonceDTO) {
        try {
            AnnonceResponseDTO updatedAnnonce = annonceService.updateAnnonce(id, updatedAnnonceDTO);
            return new ResponseEntity<>(updatedAnnonce, HttpStatus.OK);
        } catch (AnnonceNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        try {
            annonceService.deleteAnnonce(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AnnonceNotFoundException e) {
            throw e;
        }
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
