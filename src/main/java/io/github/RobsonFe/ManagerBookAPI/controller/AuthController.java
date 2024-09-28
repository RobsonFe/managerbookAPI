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

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API para autenticação de usuários")
public class AuthController {

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
        MessageResponseDTO<UserDTO> user = userService.create(userDTO);
        return ResponseEntity.ok(user);
    }

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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("refresh", refreshToken);
        tokens.put("access", accessToken);

        return ResponseEntity.ok(tokens);
    }

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
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                long expirationTime = jwtUtil.extractExpiration(jwt).getTime();
                tokenBlacklistService.addToBlacklist(jwt, expirationTime);
                return ResponseEntity.ok("Logged out successfully");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid token");
            }
        }
        return ResponseEntity.badRequest().body("Invalid Authorization header");
    }

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
        String refreshToken = request.get("refresh");
        if (refreshToken != null && jwtUtil.validateToken(refreshToken, userDetailsService.loadUserByUsername(jwtUtil.extractUsername(refreshToken)))) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(refreshToken));
            String newAccessToken = jwtUtil.generateAccessToken(userDetails);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access", newAccessToken);

            return ResponseEntity.ok(tokens);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
}
