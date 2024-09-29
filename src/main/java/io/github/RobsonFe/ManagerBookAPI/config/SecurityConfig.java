package io.github.RobsonFe.ManagerBookAPI.config;

// Importações das classes e interfaces necessárias para a configuração do Spring Security
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.github.RobsonFe.ManagerBookAPI.auth.JwtRequestFilter;

// @Configuration indica que esta classe contém definições de configuração para o Spring
@Configuration
// @EnableWebSecurity habilita o suporte à segurança no Spring e permite personalizar as configurações de segurança
@EnableWebSecurity
public class SecurityConfig {

    // Injetamos o filtro JwtRequestFilter, que será responsável por validar o token JWT em cada requisição
    private final JwtRequestFilter jwtRequestFilter;

    // O construtor recebe o filtro JWT como dependência para ser usado na configuração da segurança
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // Bean que configura a cadeia de filtros de segurança para as requisições HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita a proteção contra CSRF, já que a aplicação não usa sessões para autenticação, mas sim tokens JWT
                .csrf(csrf -> csrf.disable())
                // Configura quais rotas serão protegidas e quais serão públicas
                .authorizeHttpRequests(customizer -> customizer
                // Essas rotas são públicas e não requerem autenticação
                .requestMatchers(
                        "/swagger-ui/**", // Documentação Swagger
                        "/v3/api-docs/**", // API Docs
                        "/swagger-ui.html", // Página principal do Swagger
                        "/docs-ManagerBookAPI/**", // Documentação personalizada
                        "/api/auth/**" // Endpoints de autenticação como login/registro
                ).permitAll()
                // A rota de logout exige que o usuário esteja autenticado
                .requestMatchers("/api/auth/logout").authenticated()
                // Qualquer outra requisição deve ser autenticada
                .anyRequest().authenticated()
                )
                // Configura o gerenciamento de sessão como stateless, ou seja, sem armazenar informações de sessão no servidor
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Adiciona o JwtRequestFilter antes do filtro padrão de autenticação do Spring, para que o JWT seja verificado
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                // Finaliza a construção da cadeia de filtros de segurança
                .build();
    }

    // Bean que define o PasswordEncoder usado para criptografar e verificar as senhas no sistema
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usa o algoritmo BCrypt para codificar as senhas
    }

    // Bean que expõe o AuthenticationManager, responsável por gerenciar o processo de autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
