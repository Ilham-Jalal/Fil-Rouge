package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final UserDetailsService userDetailsService;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String JWT_EXPIRED_MESSAGE = "JWT token is expired";
    private static final String INVALID_SIGNATURE_MESSAGE = "Invalid JWT signature";
    private static final String AUTHENTICATION_FAILED_MESSAGE = "Authentication failed";

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
                SecretKey key = (SecretKey) JwtUtil.SECRET_KEY; // Directly use the SecretKey

                // Parse the JWT
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                logger.warn(JWT_EXPIRED_MESSAGE);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, JWT_EXPIRED_MESSAGE);
                return;
            } catch (io.jsonwebtoken.security.SecurityException e) {
                logger.warn(INVALID_SIGNATURE_MESSAGE);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_SIGNATURE_MESSAGE);
                return;
            } catch (Exception e) {
                logger.error(AUTHENTICATION_FAILED_MESSAGE, e);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, AUTHENTICATION_FAILED_MESSAGE);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValidToken(String authorizationToken) {
        return authorizationToken != null && authorizationToken.startsWith(BEARER_PREFIX);
    }
}
