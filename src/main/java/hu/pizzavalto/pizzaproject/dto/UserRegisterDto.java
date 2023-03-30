package hu.pizzavalto.pizzaproject.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    @Size(min = 2, max = 50)
    private String first_name;
    @Size(min = 2, max = 50)
    private String last_name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

}
