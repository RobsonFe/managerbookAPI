package io.github.RobsonFe.ManagerBookAPI.service;

import io.github.RobsonFe.ManagerBookAPI.entity.UserModel; // Entidade do banco de dados
import io.github.RobsonFe.ManagerBookAPI.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User; // Importando o User do Spring Security
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Construindo o objeto UserDetails usando o User do Spring Security
        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword()) // A senha deve estar criptografada no banco
                .roles("USER") // Defina as roles conforme necessário
                .build();
    }
}
