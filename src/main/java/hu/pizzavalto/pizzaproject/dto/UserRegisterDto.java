package hu.pizzavalto.pizzaproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validációval egybe kötött Felhasználó DataTransferObjekt.
 * Lombok használatával nincs dokumentálva a konstruktor illetve a getter-setter-ek.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    /**
     * Validálja, hogy a keresztnév min 2 maximum 50 karakter hosszú és nem üres.
     */
    @Size(min = 2, max = 50)
    private String first_name;
    /**
     * Validálja, hogy a vezetéknév min 2 maximum 50 karakter hosszú és nem üres.
     */
    @Size(min = 2, max = 50)
    private String last_name;
    /**
     * Validálja, hogy az email email típusú és nem üres.
     */
    @Email
    @NotBlank
    private String email;
    /**
     * Validálja, hogy a jelszó min 6 maximum 255 karakter hosszú és nem üres.
     */
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;
}
