package hu.pizzavalto.pizzaproject.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Felhasználó osztály.
 */
@Entity
@Table(name="users")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
@Builder
@Getter
public class User {
    /**
     * Magától generáltatott felhasználó id.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Felhasználó keresztnév.
     */
    @Column(name = "first_name")
    private String first_name;
    /**
     * Felhasználó vezetéknév.
     */
    @Column(name = "last_name")
    private String last_name;
    /**
     * Felhasználó email címe.
     */
    @Column(name = "email", unique = true)
    private String email;
    /**
     * Felhasználó jelszava.
     */
    @Column(name = "password")
    private String password;
    /**
     * Felhasználó szerepköre.
     */
    @Enumerated(EnumType.STRING)
    private Role role;
}
