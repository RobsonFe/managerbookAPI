package io.github.RobsonFe.ManagerBookAPI.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    private final PasswordEncoder passwordEncoder;

    @Lazy
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        // Loga o nome de usuário e a senha bruta
        logger.debug("Username: {}", username);
        logger.debug("Raw Password: {}", rawPassword);

        // Normalmente, o usuário seria recuperado de um banco de dados aqui.
        // Mas, para fins de teste, estamos usando um nome de usuário e senha fixos.
        String storedPassword = passwordEncoder.encode("admin"); // Senha que estaria no banco de dados ou propriedades

        // Loga a senha armazenada (codificada)
        logger.debug("Encoded Password in System: {}", storedPassword);

        // Verifica se o nome de usuário é "admin" e a senha bruta corresponde à senha codificada armazenada
        if ("admin".equals(username) && passwordEncoder.matches(rawPassword, storedPassword)) {
            return new UsernamePasswordAuthenticationToken(username, rawPassword, Collections.emptyList());
        } else {
            // Lança uma exceção se a autenticação falhar
            throw new AuthenticationException("Falha na autenticação!") {};
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Verifica se a autenticação é do tipo UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
