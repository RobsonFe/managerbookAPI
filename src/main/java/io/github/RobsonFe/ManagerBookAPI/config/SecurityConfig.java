package io.github.RobsonFe.ManagerBookAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // Rotas para usuários e admins
                        .requestMatchers("/swagger-ui.html","/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**",
                                "/v2/api-docs/**", "/swagger-resources/**", "/webjars/**", "/docs-book", "/docs-books.html").permitAll() // Permite acesso ao Swagger
                        .requestMatchers(EndpointRequest.to("health")).permitAll() // Permite acesso ao endpoint de health check
                        .requestMatchers("/api/public/**").permitAll() // Rotas públicas
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Rotas apenas para admin
                        .anyRequest().authenticated() // Todas as outras rotas requerem autenticação
                )
                .formLogin(form -> form
                        .loginPage("/login") // Página de login customizada
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redireciona após logout
                )
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para facilitar o uso da API
                .httpBasic(Customizer.withDefaults()); // Habilita autenticação básica com as configurações padrão

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usa BCrypt para hash de senhas
    }
}
