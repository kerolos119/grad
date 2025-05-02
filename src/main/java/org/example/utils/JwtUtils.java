package org.example.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.example.document.Users;
import org.example.exception.UserException;
import org.example.model.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String base64Secret;

    @Value("${jwt.expirationMs}")
    private Long expirationMs;
    
    @Value("${jwt.refresh.expirationMs:604800000}") // 7 days default
    private Long refreshExpirationMs;
    
    @Value("${jwt.issuer:EyesOnPlants}")
    private String issuer;

    /**
     * Gets a signing key from the base64-encoded secret
     */
    private SecretKey getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Generates an access token for a user
     */
    public String generateAccessToken(Users user) {
        return generateToken(user, expirationMs, "ACCESS");
    }
    
    /**
     * Generates a refresh token for a user
     */
    public String generateRefreshToken(Users user) {
        return generateToken(user, refreshExpirationMs, "REFRESH");
    }
    
    /**
     * Generate a JWT token with the specified claims and expiration
     */
    private String generateToken(Users user, Long expiration, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().toString());
        claims.put("type", tokenType);
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        
        try {
            return Jwts.builder()
                    .claims(claims)
                    .subject(user.getEmail())
                    .issuedAt(new Date())
                    .issuer(issuer)
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception ex) {
            logger.error("Failed to generate token: {}", ex.getMessage(), ex);
            throw new UserException("Failed to generate authentication token");
        }
    }

    /**
     * Validates a token
     */
    public Boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }
    
    /**
     * Checks if a token is a refresh token
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return "REFRESH".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts user information from a token
     */
    public TokenInfo extractInfo(String token) throws JwtException {
        Claims claims = extractAllClaims(token);
        
        String roleString = claims.get("role", String.class);
        
        return TokenInfo.builder()
                .email(claims.get("email", String.class))
                .userId(claims.get("userId", Long.class))
                .roles(roleString)
                .firstName(claims.getOrDefault("firstName", "").toString())
                .lastName(claims.getOrDefault("lastName", "").toString())
                .tokenType(claims.getOrDefault("type", "ACCESS").toString())
                .build();
    }
    
    /**
     * Extract all claims from a token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Extract expiration date from a token
     */
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    
    /**
     * Check if a token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
