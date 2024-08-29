package com.example.demo.controllers;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Annonce;

import com.example.demo.models.User;
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
    public ResponseEntity<Annonce> createAnnonce(@RequestBody Annonce annonce) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        Annonce createdAnnonce = annonceService.createAnnonce(annonce, user);
        return new ResponseEntity<>(createdAnnonce, HttpStatus.CREATED);
    }

    @GetMapping("/annonces")
    public ResponseEntity<List<Annonce>> getAnnocesByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur user = optionalUser.get();
        List<Annonce> annonces = annonceService.getAnnonceByUser(user.getId());
        return ResponseEntity.ok(annonces);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Annonce> updateAnnonce(@PathVariable Long id, @RequestBody Annonce updatedAnnonce) {
        Annonce annonce = annonceService.updateAnnonce(id, updatedAnnonce);
        return new ResponseEntity<>(annonce, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        annonceService.deleteAnnonce(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Annonce>> getAllAnnonces() {
        List<Annonce> annonces = annonceService.findAllAnnonces();
        return new ResponseEntity<>(annonces, HttpStatus.OK);
    }

//    @GetMapping("/category/{category}")
//    public ResponseEntity<List<Annonce>> getAnnoncesByCategory(@PathVariable Categorie category) {
//        List<Annonce> annonces = annonceService.findAnnoncesByCategory(category);
//        return new ResponseEntity<>(annonces, HttpStatus.OK);
//    }
//
//    @GetMapping("/disponibilite/{disponibilite}")
//    public ResponseEntity<List<Annonce>> getAnnoncesByDisponibilite(@PathVariable Disponibilite disponibilite) {
//        List<Annonce> annonces = annonceService.findAnnoncesByDisponibilite(disponibilite);
//        return new ResponseEntity<>(annonces, HttpStatus.OK);
//    }

    @GetMapping("/search")
    public List<Annonce> searchAnnonces(@RequestParam(required = false) String title,
                                        @RequestParam(required = false) String description,
                                        @RequestParam(required = false) Categorie category,
                                        @RequestParam(required = false) Double minPrice,
                                        @RequestParam(required = false) Double maxPrice) {

        double minPriceValue = (minPrice != null) ? minPrice : 0.0;
        double maxPriceValue = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;

        return annonceService.searchAnnonces(title, description, category, minPriceValue, maxPriceValue);
    }

}
