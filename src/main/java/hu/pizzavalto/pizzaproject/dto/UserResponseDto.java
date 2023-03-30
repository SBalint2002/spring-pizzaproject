package hu.pizzavalto.pizzaproject.dto;

import hu.pizzavalto.pizzaproject.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private Role role;

}
