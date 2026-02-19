package com.logicoy.smartprescriptiontracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Responsible for:
 * - Generating JWT after successful login
 * - Extracting username from token
 * - Validating token
 */
@Service
public class JwtService {

    private static final Logger log =
            LoggerFactory.getLogger(JwtService.class);

    // ⚠ Move to environment variable in real project
    private final String SECRET =
            "mysecretkeymysecretkeymysecretkey123456";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generates token valid for 1 hour.
     */
    public String generateToken(String username) {

        log.info("Generating JWT for user={}", username);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                + 3600000))
                .signWith(getSignKey(),
                        SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
