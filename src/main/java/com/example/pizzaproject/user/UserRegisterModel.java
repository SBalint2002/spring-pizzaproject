package com.example.pizzaproject.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterModel {

    private String first_name;
    private String last_name;
    private String email;
    private String password;

}
