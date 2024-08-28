package com.example.demo.controllers;

import com.example.demo.config.JwtAuth;
import com.example.demo.dto.LoginRequest;
import com.example.demo.models.Admin;
import com.example.demo.models.Livreur;
import com.example.demo.models.User;
import com.example.demo.models.Utilisateur;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Accès refusé : rôle non autorisé");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String token = JwtAuth.generateToken(user.getUsername(), user.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }
}
