package com.example.demo.controllers;

import com.example.demo.dto.SignUpRequest;
import com.example.demo.enums.Role;
import com.example.demo.models.Annonce;
import com.example.demo.models.User;
import com.example.demo.models.Utilisateur;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            User user = userService.signUpUser(signUpRequest);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/add/{role}")
    public ResponseEntity<User> addUserByAdmin(@PathVariable Role role, @RequestBody SignUpRequest signUpRequest) {
        try {
            User user = userService.addUserByAdmin(role, signUpRequest);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Utilisateur> getUserById(@PathVariable Long id) {
        Optional<Utilisateur> user = userService.getUtilisateurById(id);
        return user.map(utilisateur -> new ResponseEntity<>(utilisateur, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<Utilisateur>> getAllUsers() {
        List<Utilisateur> users = userService.getAllUtilisateurs();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUtilisateur(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{id}/ventes")
    public ResponseEntity<List<Annonce>> getHistoriqueVentes(@PathVariable Long id) {
        try {
            List<Annonce> ventes = userService.getHistoriqueVentes(id);
            return new ResponseEntity<>(ventes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{id}/achats")
    public ResponseEntity<List<Annonce>> getHistoriqueAchats(@PathVariable Long id) {
        try {
            List<Annonce> achats = userService.getHistoriqueAchats(id);
            return new ResponseEntity<>(achats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
