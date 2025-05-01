package org.example.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MalformedKeyException;
import org.example.document.Users;
import org.example.exception.UserException;
import org.example.model.TokenInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.example.document.Users;


import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String basse64Secret;

    @Value("${jwt.expirationMs}")
    private Long expirationMs;


    private SecretKey getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(basse64Secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String generate(Users admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", admin.getUserId());
        claims.put("email", admin.getEmail());

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception ex) {
            System.err.println("failed to generate token");
            System.err.println(ex.getMessage());
            throw new UserException("failed to generate token");
        }

    }

    public Boolean isValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedKeyException e) {
        } catch (ExpiredJwtException e) {
        } catch (UnsupportedJwtException e) {
        } catch (IllegalArgumentException e) {
        }
        return false;
    }


    public TokenInfo extractInfo(String token) throws JwtException {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return TokenInfo.builder()
                .email(claims.get("email", String.class))
                .userId(claims.get("userId", String.class))
                .build();
    }


}
