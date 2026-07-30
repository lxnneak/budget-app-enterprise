package com.linneakarlsson.budget_app_enterpise.security;

import com.linneakarlsson.budget_app_enterpise.model.customUser.authority.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}") // found in application-local.properties
    private String base64EncodedSecretKey;

    @Value("${app.jwt.expiration-ms}") // found in application.properties
    private int jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(UserDetails userDetails) { // TODO - CustomUserDetails
        logger.debug("Generating JWT for user: {} with roles: {}", userDetails.getUsername(), userDetails.getAuthorities());

        List<String> roles = userDetails.getAuthorities().stream().map(
                grantedAuthority -> grantedAuthority.getAuthority()
        ).toList();

        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();

        logger.info("JWT generated successfully for user: {}", userDetails.getUsername());
        return token;
    }

    public String getUsernameFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.getSubject();
            logger.debug("Extracted username '{}' from JWT token", username);
            return username;

        } catch (Exception e) {
            logger.warn("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    public Set<UserRole> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<?> authoritiesClaim = claims.get("authorities", List.class);

        if (authoritiesClaim == null || authoritiesClaim.isEmpty()) {
            logger.warn("No authorities found in JWT token");
            return Set.of();
        }


        Set<UserRole> roles = authoritiesClaim.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(role -> role.replace("ROLE_", ""))
                .map(String::toUpperCase)
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());

        logger.debug("Extracted roles from JWT token: {}", roles);
        return roles;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);

            logger.debug("JWT validation succeeded");
            return true;

        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        }

        return false;
    }

    // Helper: Extract JWT from cookie
    String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("authToken".equals(cookie.getName())) {     // Cookie should be named authToken
                return cookie.getValue();
            }
        }
        return null;
    }

    // Helper: Extract JWT from Authorization header
    String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
