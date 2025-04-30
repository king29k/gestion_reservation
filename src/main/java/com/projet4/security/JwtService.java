package com.projet4.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 heures
    private static final String SECRET_KEY = "maSuperCleDeSecuriteJwtUltraSecreteEtLongue1234567890";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Générer un token JWT avec le rôle inclus
    public String generateToken(UserDetails userDetails) {
        // Par exemple on peut récupérer le rôle via userDetails ou ne pas l’inclure dans le token
        Map<String, Object> claims = new HashMap<>();
        // claims.put("role", ... ); // si tu veux ajouter le rôle au JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraire le nom d'utilisateur (email) depuis le token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Vérifier si le token est valide pour l'utilisateur
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Vérifie si le token a expiré
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // Extraire tous les claims du token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extraire un claim spécifique via une fonction (ex : username, expiration, rôle, etc.)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraire le rôle depuis le token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
