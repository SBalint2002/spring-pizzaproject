package com.example.pizzaproject.user;

import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateModel {
    @NotBlank
    @Nullable
    @Size(min = 2, max = 50)
    private String first_name;

    @NotBlank
    @Nullable
    @Size(min = 2, max = 50)
    private String last_name;

    @Email
    @Nullable
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    @Nullable
    private String password;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Nullable
    private Role role;
}
