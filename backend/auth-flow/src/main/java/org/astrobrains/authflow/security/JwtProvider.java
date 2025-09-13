package org.astrobrains.authflow.security;

import org.astrobrains.authflow.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProperties jwtProperties;
    private SecretKey key;

    @PostConstruct
    private void initKey() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a signed JWT token for the authenticated user.
     */
    public String generateToken(Authentication auth) {
        String roles = extractAuthorities(auth.getAuthorities());

        return Jwts.builder()
                .setSubject(auth.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .claim(CLAIM_EMAIL, auth.getName())
                .claim(CLAIM_AUTHORITIES, roles)
                .signWith(key)
                .compact();
    }

    /**
     * Extracts the email from a valid JWT token.
     */
    public String getEmailFromToken(String token) {
        Claims claims = parseClaims(stripBearerPrefix(token));
        return claims.get(CLAIM_EMAIL, String.class);
    }

    /**
     * Extracts authorities from a valid JWT token.
     */
    public String getAuthoritiesFromToken(String token) {
        Claims claims = parseClaims(stripBearerPrefix(token));
        return claims.get(CLAIM_AUTHORITIES, String.class);
    }

    /**
     * Validate token (expiration + signature).
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(stripBearerPrefix(token));
            return true;
        } catch (Exception ex) {
            log.warn("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }

    // ----------------- HELPERS METHODS -----------------

    /**
     * Parses the claims from a JWT token.
     *
     * @param token the JWT token
     * @return the parsed claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * Extracts the authorities from the collection of granted authorities and
     * concatenates them into a single string separated by commas.
     *
     * @param authorities the collection of granted authorities
     * @return the extracted authorities as a string
     */
    private String extractAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }


    /**
     * Removes the "Bearer" prefix from the token, if present.
     *
     * @param token the token to strip the prefix from
     * @return the token with the "Bearer" prefix removed, or the token as-is if it does not start with "Bearer"
     */
    private String stripBearerPrefix(String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return token; // return as-is if already clean
    }
}
