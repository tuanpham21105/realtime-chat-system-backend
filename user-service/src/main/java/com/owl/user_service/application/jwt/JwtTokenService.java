package com.owl.user_service.application.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.owl.user_service.persistence.jpa.entity.RefreshToken;
import com.owl.user_service.persistence.jpa.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {
    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.access-expiration}")
    private long accessExpiration;

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    
    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String userId, String role) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(refreshExpiration);

        return Jwts.builder()
            .subject(userId)
            .claim("type", "access")
            .claim("role", role)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(key())
            .compact();
    }

    public String generateRefreshToken(String userId) {
        String jti = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(refreshExpiration);

        refreshTokenRepository.save(new RefreshToken(jti, userId, expiration));

        return Jwts.builder()
            .subject(userId)
            .id(jti)
            .claim("type", "refresh")
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(key())
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith((SecretKey) key())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
    
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith((SecretKey) key())   // signature verification
                .build()
                .parseSignedClaims(token)        // JWS
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Expired token"
            );
        } catch (JwtException e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid token"
            );
        }
    }

    public Claims validateAccessToken(String token) {
        Claims claims = validateToken(token);

        if (!"access".equals(claims.get("type", String.class))) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Not an access token"
            );
        }

        return claims;
    }

    public Claims validateRefreshToken(String token) {
        Claims claims = validateToken(token);

        if (!"refresh".equals(claims.get("type", String.class))) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Not a refresh token"
            );
        }

        return claims;
    }
    
}
