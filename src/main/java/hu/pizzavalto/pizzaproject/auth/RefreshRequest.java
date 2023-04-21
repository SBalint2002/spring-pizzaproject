package hu.pizzavalto.pizzaproject.auth;

import lombok.*;

/**
 * RefreshKérés osztály.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
public class RefreshRequest {
    /**
     * RefreshToken Stringként való tárolása.
     */
    private String refreshToken;
}
