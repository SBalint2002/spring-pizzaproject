package hu.pizzavalto.pizzaproject.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
public class RefreshRequest {
    private String refreshToken;
}
