package hu.pizzavalto.pizzaproject.auth;

import hu.pizzavalto.pizzaproject.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * RefreshHasználhatósági osztály.
 */
@Component
public class RefreshUtil {
    /**
     * RefreshTokenhez tartozó kulcs Stringként való tárolása.
     */
    private static final String secret = "olUo6BXAlnmeWQJA5NLMy8rISeO4qfkhlPL9P6FM";
    /**
     * RefreshToken lejáratért felelős long milliszekundumban.
     */
    private static final long expirationMs = 2592000000L; // 30 nap
    //private static final long expirationMs = 60000; // 1 perc

    /**
     * Ez a funkció hozza létre a refreshTokent.
     * @param user Felhasználó típusú adatot vár.
     * @return Stringket ad vissza a kérés sikerességéről.
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
     * Ez a funkció adja vissza a refreshTokenből az adott felhasználó kinyerhető email címét.
     * @param token String típusú adatot kér, ami egy token.
     * @return Strinként adja vissza az email címet.
     */
    public static String getEmailFromRefreshToken(String token) {
        byte[] apiKeySecretBytes = secret.getBytes();
        return Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Ez a funkció nézi, meg, hogy az adott token lejárt-e.
     * @param token String típusú adatot kér, ami token.
     * @return Vissza küldi, hogy a token még érvényes-e.
     */
    public static boolean isExpired(String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException error){
            return true;
        }

    }
}

