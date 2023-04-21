package hu.pizzavalto.pizzaproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Validációval egybe kötött Rendelés DataTransferObjekt.
 * Lombok használatával nincs dokumentálva a konstruktor illetve a getter-setter-ek.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    /**
     * Validálja, hogy a helyszín min 2 maximum 256 karakter hosszú és nem lehet üres.
     */
    @NotBlank
    @Size(min = 2, max = 256)
    private String location;
    /**
     * Validálja, hogy a telefonszám min 8 maximum 10 karakter hosszú és nem lehet üres.
     */
    @NotBlank
    @Size(min = 8, max = 10)
    private String phoneNumber;
    /**
     * Validálja, hogy a Lista csak Pizza típusú adatokat tárolhat és, hogy nem üres.
     */
    @NotBlank
    private List<Long> pizzaIds;
}
