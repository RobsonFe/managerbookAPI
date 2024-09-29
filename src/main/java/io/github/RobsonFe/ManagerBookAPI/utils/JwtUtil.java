package io.github.RobsonFe.ManagerBookAPI.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Utilitário responsável por gerar e validar tokens JWT (JSON Web Token).
 */
@Component
public class JwtUtil {

    // A chave secreta utilizada para assinar os tokens JWT
    @Value("${jwt.secret}")
    private String secret;

    // Tempo de expiração do token de acesso (em milissegundos)
    @Value("${jwt.access.expiration}")
    private Long accessExpirationTime;

    // Tempo de expiração do token de atualização (em milissegundos)
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpirationTime;

    /**
     * Gera a chave de assinatura utilizando o segredo JWT.
     *
     * @return A chave de assinatura gerada.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrai o nome de usuário de um token JWT.
     *
     * @param token O token JWT.
     * @return O nome de usuário extraído.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai a data de expiração de um token JWT.
     *
     * @param token O token JWT.
     * @return A data de expiração extraída.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai um valor específico (claim) de um token JWT.
     *
     * @param <T> O tipo do valor da claim.
     * @param token O token JWT.
     * @param claimsResolver A função que resolve a claim.
     * @return O valor da claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai todas as claims de um token JWT.
     *
     * @param token O token JWT.
     * @return As claims contidas no token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica se um token JWT está expirado.
     *
     * @param token O token JWT.
     * @return {@code true} se o token estiver expirado; caso contrário,
     * {@code false}.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Gera um token de acesso (access token) para um usuário.
     *
     * @param userDetails Os detalhes do usuário.
     * @return O token de acesso gerado.
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), accessExpirationTime);
    }

    /**
     * Gera um token de atualização (refresh token) para um usuário.
     *
     * @param userDetails Os detalhes do usuário.
     * @return O token de atualização gerado.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshExpirationTime);
    }

    /**
     * Cria um token JWT com base em claims, assunto (subject) e tempo de
     * expiração.
     *
     * @param claims As claims que serão incluídas no token.
     * @param subject O assunto (geralmente o nome de usuário) do token.
     * @param expirationTime O tempo de expiração do token.
     * @return O token JWT gerado.
     */
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Valida um token JWT verificando se o nome de usuário do token coincide
     * com o do usuário e se o token não está expirado.
     *
     * @param token O token JWT.
     * @param userDetails Os detalhes do usuário.
     * @return {@code true} se o token for válido; caso contrário,
     * {@code false}.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
