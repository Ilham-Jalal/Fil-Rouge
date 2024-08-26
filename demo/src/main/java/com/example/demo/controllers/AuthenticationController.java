package com.example.demo.controllers;

import com.example.demo.config.JwtAuth;
import com.example.demo.dto.LoginRequest;
import com.example.demo.enums.Role;
import com.example.demo.models.Admin;
import com.example.demo.models.Livreur;
import com.example.demo.models.User;
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
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login request received for username: " + loginRequest.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("Authentication successful for username: " + loginRequest.getUsername());

            Optional<User> userOpt =  userService.findUtilisateurByUsername(loginRequest.getUsername())
                    .map(utilisateur -> {
                        System.out.println("Utilisateur found");
                        return (User) utilisateur;
                    })
                    .or(() -> userService.findLivreurByUsername(loginRequest.getUsername())
                            .map(livreur -> {
                                System.out.println("Livreur found");
                                return (User) livreur;
                            }))
                    .or(() -> userService.findAdminByUsername(loginRequest.getUsername())
                            .map(admin -> {
                                System.out.println("Admin found");
                                return (User) admin;
                            }));

            if (!userOpt.isPresent()) {
                System.out.println("No user found, access denied");
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Access denied: Role not allowed");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            User user = userOpt.get();

            String token = JwtAuth.generateToken(user.getUsername(), user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole());

            System.out.println("Login successful for role: " + user.getRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}