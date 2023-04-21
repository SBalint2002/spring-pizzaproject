package hu.pizzavalto.pizzaproject.auth;

import hu.pizzavalto.pizzaproject.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Access token osztálya amiben generálja a token-t, elhelyezi benne a felhasználó azonosítóját és szerepkörét,
 * ellenőrzi érvényességét, beállítja lejárati idejét (5 perc),
 * ellenőrzi adminisztrátori tulajdonságát illetve kicsomagolja a felhasználó e-mail címét.
 */
@Component
public class AccessUtil {
    /**
     * Token titkosításához használt kulcs, String változó.
     */
    private static final String secret = "1KoOpQKyVv5Yxkj4LHxjBgLjpczO6L8P0lq87LDi";

    /**
     * Lejáratért felelős long típusú érték milliszekundumban.
     */
    private static final long expirationMs = 300000; // 5 perc
    //private static final long expirationMs = 60000; // 1 perc
    //private static final long expirationMs = 10000; // 10 másodperc

    /**
     * Ez a funkció hozza létre az access token-t a felhasználóhoz.
     *
     * @param user Felhasználó objektum.
     * @return Visszaadja a létrehozott access token-t szöveg típusban.
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
     * Ez a funkció kiszedi az e-mail címet a tokenből.
     *
     * @param token access token.
     * @return Vissza küldi a tokenben található e-meail címet szövegként.
     */
    public static String getEmailFromJWTToken(String token) {
        byte[] apiKeySecretBytes = secret.getBytes();
        return Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Ez a funkció ellenőrzi, hogy a felhasználó rendelkezik -e admin jogosultsággal.
     *
     * @param token access token.
     * @return Válaszként küldi el, hogy a felhasználó rendelkezik -e admin jogosultsággal (true/false).
     */
    public static Boolean isAdminFromJWTToken(String token) {
        byte[] apiKeySecretBytes = secret.getBytes();
        Claims claims = Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(token).getBody();
        String role = (String) claims.get("role");
        return role != null && role.equals("ADMIN");
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
