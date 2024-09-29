package io.github.RobsonFe.ManagerBookAPI.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.UserDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.UserLoginDTO;
import io.github.RobsonFe.ManagerBookAPI.service.TokenBlacklistService;
import io.github.RobsonFe.ManagerBookAPI.service.UserService;
import io.github.RobsonFe.ManagerBookAPI.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// Esta classe é um controlador REST responsável pelas operações de autenticação da API.
// Usa o Spring Security para autenticar usuários e gerar tokens JWT.
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API para autenticação de usuários")
public class AuthController {

    // Dependências injetadas via construtor, responsáveis por diversas operações:
    // - AuthenticationManager: Gerencia o processo de autenticação.
    // - UserDetailsService: Recupera os detalhes de um usuário.
    // - JwtUtil: Gera e valida tokens JWT.
    // - UserService: Gerencia operações de criação de usuários.
    // - TokenBlacklistService: Armazena tokens inválidos (lista negra).
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UserService userService,
            TokenBlacklistService tokenBlacklistService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // Endpoint para registrar um novo usuário.
    // Aceita uma requisição POST com um objeto UserDTO válido.
    @Operation(summary = "Registrar um novo usuário", description = "Cria uma nova conta de usuário na aplicação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso",
                content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        // Chama o serviço para criar o usuário e retorna uma mensagem de sucesso.
        MessageResponseDTO<UserDTO> user = userService.create(userDTO);
        return ResponseEntity.ok(user);
    }

    // Endpoint para realizar login.
    // Aceita uma requisição POST com as credenciais do usuário e retorna tokens de acesso e refresh.
    @Operation(summary = "Realiza login", description = "Autentica o usuário e gera tokens de acesso e refresh")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                content = @Content(schema = @Schema(example = "{\"access\": \"<token>\", \"refresh\": \"<token>\"}"))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginDTO loginDTO) {
        try {
            // Autentica o usuário usando as credenciais fornecidas.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Se as credenciais forem inválidas, retorna erro 401.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // Se a autenticação for bem-sucedida, gera os tokens de acesso e refresh.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // Retorna os tokens como resposta.
        Map<String, String> tokens = new HashMap<>();
        tokens.put("refresh", refreshToken);
        tokens.put("access", accessToken);

        return ResponseEntity.ok(tokens);
    }

    // Endpoint para realizar logout.
    // O token de acesso é invalidado adicionando-o à lista negra.
    @Operation(summary = "Realiza logout", description = "Invalida o token de acesso atual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Token inválido",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        // Verifica se o cabeçalho Authorization contém um token JWT.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                // Extrai o tempo de expiração do token e o adiciona à lista negra.
                long expirationTime = jwtUtil.extractExpiration(jwt).getTime();
                tokenBlacklistService.addToBlacklist(jwt, expirationTime);
                return ResponseEntity.ok("Logged out successfully");
            } catch (Exception e) {
                // Se houver algum erro, retorna uma resposta de token inválido.
                return ResponseEntity.badRequest().body("Invalid token");
            }
        }
        return ResponseEntity.badRequest().body("Invalid Authorization header");
    }

    // Endpoint para atualizar o token de acesso usando um token de refresh válido.
    @Operation(summary = "Atualiza o token de acesso", description = "Gera um novo token de acesso a partir do token de refresh válido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token de acesso atualizado com sucesso",
                content = @Content(schema = @Schema(example = "{\"access\": \"<new_access_token>\"}"))),
        @ApiResponse(responseCode = "401", description = "Token de refresh inválido",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        // Extrai o token de refresh da requisição.
        String refreshToken = request.get("refresh");
        // Valida o token de refresh e, se for válido, gera um novo token de acesso.
        if (refreshToken != null && jwtUtil.validateToken(refreshToken, userDetailsService.loadUserByUsername(jwtUtil.extractUsername(refreshToken)))) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(refreshToken));
            String newAccessToken = jwtUtil.generateAccessToken(userDetails);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access", newAccessToken);

            return ResponseEntity.ok(tokens);
        }
        // Se o token de refresh for inválido, retorna erro 401.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
}
