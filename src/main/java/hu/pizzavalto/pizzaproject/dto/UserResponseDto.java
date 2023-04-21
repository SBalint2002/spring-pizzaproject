package hu.pizzavalto.pizzaproject.dto;

import hu.pizzavalto.pizzaproject.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validációval egybe kötött FelhasználóVálasz DataTransferObjekt.
 * Lombok használatával nincs dokumentálva a konstruktor illetve a getter-setter-ek.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    /**
     * FelhasználóVálasz DataTransferObjekt id-je.
     */
    private Long id;
    /**
     * FelhasználóVálasz DataTransferObjekt keresztneve.
     */
    private String first_name;
    /**
     * FelhasználóVálasz DataTransferObjekt vezetékneve.
     */
    private String last_name;
    /**
     * FelhasználóVálasz DataTransferObjekt emailje.
     */
    private String email;
    /**
     * FelhasználóVálasz DataTransferObjekt szerepköre.
     */
    private Role role;

}
