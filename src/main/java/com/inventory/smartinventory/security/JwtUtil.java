package com.inventory.smartinventory.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class for JWT operations.
 *
 * Responsibilities:
 *   - Generate a signed JWT token from a username
 *   - Extract the username from a token
 *   - Validate a token (signature check + expiry check)
 *
 * The secret key and expiry duration are configured in application.properties:
 *   app.jwt.secret=<base64-encoded 32+ byte key>
 *   app.jwt.expiration-ms=86400000   (24 hours)
 */
@Component
public class JwtUtil {

    // Injected from application.properties
    @Value("${app.jwt.secret}")
    private String secretBase64;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    // ============================================
    // Build the signing key from the base64 secret
    // ============================================

    private Key getSigningKey() {
        // Normalize: accept standard Base64 (+/) OR URL-safe Base64 (-_),
        // then use MIME decoder which is tolerant of whitespace/line breaks.
        String normalized = secretBase64
                .replace('-', '+')
                .replace('_', '/');
        // Pad to a multiple of 4 if necessary
        int pad = normalized.length() % 4;
        if (pad == 2) normalized += "==";
        else if (pad == 3) normalized += "=";
        byte[] keyBytes = Base64.getMimeDecoder().decode(normalized);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ============================================
    // GENERATE TOKEN
    // Subject = username; includes issue + expiry timestamps
    // ============================================

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ============================================
    // EXTRACT USERNAME FROM TOKEN
    // Parses the token and returns the subject claim
    // ============================================

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // ============================================
    // VALIDATE TOKEN
    // Returns true only if the token is properly signed
    // and has not expired
    // ============================================

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            String username = extractUsername(token);
            boolean notExpired = !parseClaims(token).getExpiration().before(new Date());
            return username.equals(expectedUsername) && notExpired;
        } catch (Exception e) {
            return false;
        }
    }

    // ============================================
    // PRIVATE HELPER - parse and return all claims
    // Throws an exception if the token is invalid/expired
    // ============================================

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
