package hu.pizzavalto.pizzaproject.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JwtVálasz osztály.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    /**
     * AccessToken Stringként való tárolása.
     */
    private String accessToken;
    /**
     * RefreshToken Stringként való tárolása.
     */
    private String refreshToken;

}
