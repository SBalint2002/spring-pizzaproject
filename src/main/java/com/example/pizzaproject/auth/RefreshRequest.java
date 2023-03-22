package com.example.pizzaproject.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
public class RefreshRequest {
    private String refreshToken;
}
