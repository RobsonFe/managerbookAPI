package io.github.RobsonFe.ManagerBookAPI.auth;

// Importações necessárias para a implementação do filtro JWT
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.RobsonFe.ManagerBookAPI.service.TokenBlacklistService;
import io.github.RobsonFe.ManagerBookAPI.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component indica que esta classe será gerenciada como um bean pelo Spring, tornando-o um filtro que pode ser injetado e utilizado no pipeline de requisições
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // O UserDetailsService é usado para carregar detalhes do usuário com base no nome de usuário
    @Autowired
    private UserDetailsService userDetailsService;

    // Utilitário JWT personalizado para lidar com tokens, como extração e validação
    @Autowired
    private JwtUtil jwtUtil;

    // Serviço responsável por verificar se o token foi colocado na lista negra (blacklist)
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    // Sobrescreve o método doFilterInternal para interceptar cada requisição HTTP e aplicar a lógica de validação do token JWT
    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtém o cabeçalho de autorização da requisição HTTP
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Verifica se o cabeçalho de autorização está presente e se começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrai o token JWT removendo o prefixo "Bearer "
            jwt = authorizationHeader.substring(7);

            // Verifica se o token está na lista negra (blacklist). Se estiver, a requisição é negada.
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Retorna código 401 Unauthorized
                return;  // Interrompe a execução do filtro, não permitindo continuar o processamento
            }

            // Extrai o nome de usuário do token JWT
            username = jwtUtil.extractUsername(jwt);
        }

        // Se um nome de usuário foi extraído e não há autenticação ativa no contexto de segurança
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário a partir do UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valida o token JWT com base nos detalhes do usuário
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Cria um objeto UsernamePasswordAuthenticationToken, que contém as credenciais e permissões do usuário autenticado
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Adiciona detalhes da requisição atual ao token de autenticação
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define a autenticação no contexto de segurança, tornando o usuário autenticado para o restante da requisição
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Passa a requisição e resposta para o próximo filtro da cadeia de filtros
        chain.doFilter(request, response);
    }
}
