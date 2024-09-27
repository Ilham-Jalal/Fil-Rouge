package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public JwtAuthorizationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationToken = request.getHeader(AUTHORIZATION_HEADER);

        if (isValidToken(authorizationToken)) {
            try {
                String jwt = authorizationToken.substring(BEARER_PREFIX.length());
                SecretKey key = JwtUtil.SECRET_KEY;

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token is expired");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired");
                return;
            } catch (JwtException e) {
                logger.warn("Invalid JWT signature or token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature or token");
                return;
            } catch (Exception e) {
                logger.error("Authentication failed", e);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValidToken(String authorizationToken) {
        return authorizationToken != null && authorizationToken.startsWith(BEARER_PREFIX);
    }
}
