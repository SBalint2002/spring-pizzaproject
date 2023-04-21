package hu.pizzavalto.pizzaproject.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BiztonságiKonfigurációs osztály.
 */
@Configuration
public class SecurityConfig {
    /**
     * Ez az Objektum titkosítja a jelszót.
     * @return Válaszként a titkosított jelszót adja.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
    }
}
