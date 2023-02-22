package com.example.pizzaproject.auth;

import com.example.pizzaproject.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String secret = "1KoOpQKyVv5Yxkj4LHxjBgLjpczO6L8P0lq87LDi";
    private static final long expirationMs = 300000; // 5 perc

    public static String createJWT(User user) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + expirationMs);
        byte[] apiKeySecretBytes = secret.getBytes();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(signatureAlgorithm, apiKeySecretBytes)
                .compact();
    }

    public static String getEmailFromJWTToken(String token) {
        byte[] apiKeySecretBytes = secret.getBytes();
        return Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(token).getBody().getSubject();
    }

    public static boolean isExpired(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException error) {
            return true;
        }

    }
}
