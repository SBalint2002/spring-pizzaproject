package hu.pizzavalto.pizzaproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @NotBlank
    @Size(min = 2, max = 256)
    private String location;

    @NotBlank
    @Size(min = 2, max = 20)
    private String phoneNumber;

    @NotBlank
    private List<Long> pizzaIds;
}
