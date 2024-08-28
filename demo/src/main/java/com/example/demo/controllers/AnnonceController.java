package com.example.demo.controllers;

import com.example.demo.enums.Categorie;
import com.example.demo.enums.Disponibilite;
import com.example.demo.models.Annonce;

import com.example.demo.services.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/api/annonces")
@CrossOrigin(origins = "http://localhost:4200")
public class AnnonceController {

    private final AnnonceService annonceService;

    @Autowired
    public AnnonceController(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    @PostMapping
    public ResponseEntity<Annonce> createAnnonce(@RequestBody Annonce annonce) {
        Annonce createdAnnonce = annonceService.createAnnonce(annonce);
        return new ResponseEntity<>(createdAnnonce, HttpStatus.CREATED);
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

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Annonce>> getAnnoncesByCategory(@PathVariable Categorie category) {
        List<Annonce> annonces = annonceService.findAnnoncesByCategory(category);
        return new ResponseEntity<>(annonces, HttpStatus.OK);
    }

    @GetMapping("/disponibilite/{disponibilite}")
    public ResponseEntity<List<Annonce>> getAnnoncesByDisponibilite(@PathVariable Disponibilite disponibilite) {
        List<Annonce> annonces = annonceService.findAnnoncesByDisponibilite(disponibilite);
        return new ResponseEntity<>(annonces, HttpStatus.OK);
    }
}
