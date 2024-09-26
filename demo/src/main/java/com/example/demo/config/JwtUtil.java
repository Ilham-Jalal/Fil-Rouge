package com.example.demo.config;

import com.example.demo.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.security.Keys.secretKeyFor;

@Service
public final class JwtUtil {
    public static final Key SECRET_KEY;

    static {
        SECRET_KEY = secretKeyFor(SignatureAlgorithm.HS256);
    }

    private JwtUtil() {}
    public static String generateToken(String username, Role role) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .claim("roles", role)
                .signWith(SECRET_KEY)
                .compact();
    }
}
