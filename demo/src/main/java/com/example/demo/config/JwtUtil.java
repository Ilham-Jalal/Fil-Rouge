package com.example.demo.config;
import javax.crypto.SecretKey;


import com.example.demo.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.Keys;
import java.util.Date;

@Service
public final class JwtUtil {
    public static final SecretKey SECRET_KEY;

    static {
        SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Générer une clé secrète pour HMAC
    }

    private JwtUtil() {}

    public static String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username) // Définir le sujet comme le nom d'utilisateur
                .setIssuedAt(new Date(System.currentTimeMillis())) // Définir la date d'émission
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // Définir l'expiration à 24 heures
                .claim("roles", role) // Ajouter les rôles en tant que revendications
                .compact(); // Construire le jeton
    }
}
