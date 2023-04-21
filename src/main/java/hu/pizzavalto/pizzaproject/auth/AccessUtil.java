package hu.pizzavalto.pizzaproject.auth;

import hu.pizzavalto.pizzaproject.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * AccessTokenHasználhatósági osztály.
 */
@Component
public class AccessUtil {
    /**
     * Tokeneknek titkosításához használt kulcs, String változó.
     */
    private static final String secret = "1KoOpQKyVv5Yxkj4LHxjBgLjpczO6L8P0lq87LDi";
    /**
     * Lejáratért felelős long milliszekundumban.
     */
    private static final long expirationMs = 300000; // 5 perc
    //private static final long expirationMs = 60000; // 1 perc
    //private static final long expirationMs = 10000; // 10 másodperc
    /**
     * Ez a funkció hozza létre a JwtTokenünk a felhasználóhoz.
     * @param user Az adott felhasználó felhasználó típusú adatai.
     * @return Visszaadja a létrehozott JwtTokent Stringként.
     */
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
                .claim("role", user.getRole())
                .claim("id", user.getId())
                .compact();
    }
    /**
     * Ez a funkció szedi ki az email-t a JwtTokenből.
     * @param token JwtToken String típusú változó.
     * @return Vissza küldi a JwtTokenben található emeailt Stringként.
     */
    public static String getEmailFromJWTToken(String token) {
        byte[] apiKeySecretBytes = secret.getBytes();
        return Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(token).getBody().getSubject();
    }
    /**
     * Ez a funkció szedi ki a felhasználó szerepét a JwtTokenből.
     * @param token JwtToken String típusú változó.
     * @return Vissza küldi a JwtTokenben található felhasználó jodait Booleanként(igaz/hamis).
     */
    public static Boolean isAdminFromJWTToken(String token) {
        byte[] apiKeySecretBytes = secret.getBytes();
        Claims claims = Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(token).getBody();
        String role = (String) claims.get("role");
        return role != null && role.equals("ADMIN");
    }

    /**
     * Ez a funkció nézi, meg, hogy az adott token lejárt-e.
     * @param token JwtToken String típusú változó.
     * @return Vissza küldi, hogy a token még érvényes-e.
     */
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
