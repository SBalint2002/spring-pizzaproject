package hu.pizzavalto.pizzaproject.dto;

import hu.pizzavalto.pizzaproject.model.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validációval egybe kötött FelhasználóMódosítás DataTransferObjekt.
 * Lombok használatával nincs dokumentálva a konstruktor illetve a getter-setter-ek.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    /**
     * Validálja, hogy a keresztnév min 2 maximum 50 karakter hosszú és nem üres.
     */
    @NotBlank
    @Nullable
    @Size(min = 2, max = 50)
    private String first_name;
    /**
     * Validálja, hogy a vezetéknév min 2 maximum 50 karakter hosszú és nem üres.
     */
    @NotBlank
    @Nullable
    @Size(min = 2, max = 50)
    private String last_name;
    /**
     * Validálja, hogy az email email típusú és nem üres.
     */
    @Email
    @Nullable
    private String email;
    /**
     * Validálja, hogy a jelszó min 6 maximum 255 karakter hosszú és nem üres.
     */
    @NotBlank
    @Size(min = 6, max = 255)
    @Nullable
    private String password;
    /**
     * Validálja, hogy a szerepkör szerep típusú és nem üres.
     */
    @NotBlank
    @Enumerated(EnumType.STRING)
    @Nullable
    private Role role;
}
