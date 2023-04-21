package hu.pizzavalto.pizzaproject.auth;

import hu.pizzavalto.pizzaproject.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Refresh token osztálya amiben generálja a token-t,
 * ellenőrzi érvényességét, beállítja lejárati idejét (30 nap),
 * kicsomagolja a felhasználó e-mail címét.
 */
@Component
public class RefreshUtil {
    /**
     * Token titkosításához használt kulcs, String változó.
     */
    private static final String secret = "olUo6BXAlnmeWQJA5NLMy8rISeO4qfkhlPL9P6FM";

    /**
     * Lejáratért felelős long típusú érték milliszekundumban.
     */
    private static final long expirationMs = 2592000000L; // 30 nap
    //private static final long expirationMs = 60000; // 1 perc

    /**
     * Ez a funkció hozza létre a refresh token-t a felhasználóhoz.
     *
     * @param user Felhasználó objektum.
     * @return Visszaadja a létrehozott refresh token-t szöveg típusban.
     */
    public static String createRefreshToken(User user) {
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

    /**
     * Ez a funkció kiszedi az e-mail címet a tokenből.
     *
     * @param token refresh token.
     * @return Vissza küldi a tokenben található e-meail címet szövegként.
     */
    public static String getEmailFromRefreshToken(String token) {
        byte[] apiKeySecretBytes = secret.getBytes();
        return Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Ez a funkció ellenőrzi, hogy a token lejárt -e. True értékkel jelzi ha lejárt.
     *
     * @param token access token.
     * @return Eldönti, hogy a token lejárt -e (true/false).
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

