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

    public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private JwtUtil() {}

    public static String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .claim("roles", role)
                .signWith(SECRET_KEY)
                .compact();
    }
}
