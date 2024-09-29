package io.github.RobsonFe.ManagerBookAPI.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar tokens JWT em uma lista negra (blacklist)
 * para impedir o uso de tokens que já foram invalidados. Utiliza uma
 * `ConcurrentHashMap` para armazenar os tokens e seus respectivos tempos de
 * expiração.
 */
@Service
public class TokenBlacklistService {

    // Mapa de tokens invalidados (blacklist), mapeando o token JWT para seu tempo de expiração
    private final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();

    // Executor agendado para limpar tokens expirados da lista negra a cada hora
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * Construtor que inicializa o agendamento periódico de limpeza da lista
     * negra. O método `cleanupBlacklist` será chamado uma vez por hora para
     * remover tokens expirados.
     */
    public TokenBlacklistService() {
        // Agendar a execução de limpeza da lista negra a cada 1 hora
        scheduler.scheduleAtFixedRate(this::cleanupBlacklist, 1, 1, TimeUnit.HOURS);
    }

    /**
     * Adiciona um token à lista negra.
     *
     * @param token O token JWT a ser adicionado à blacklist.
     * @param expirationTime O tempo de expiração do token em milissegundos.
     */
    public void addToBlacklist(String token, long expirationTime) {
        blacklist.put(token, expirationTime); // Armazena o token e sua data de expiração
    }

    /**
     * Verifica se um token está presente na lista negra.
     *
     * @param token O token JWT a ser verificado.
     * @return true se o token estiver na lista negra, caso contrário false.
     */
    public boolean isBlacklisted(String token) {
        return blacklist.containsKey(token); // Retorna true se o token estiver na lista negra
    }

    /**
     * Limpa a lista negra removendo tokens que já expiraram. O método é
     * executado periodicamente pelo `scheduler`.
     */
    private void cleanupBlacklist() {
        long now = System.currentTimeMillis(); // Pega o tempo atual em milissegundos
        // Remove todos os tokens cuja expiração seja anterior ao tempo atual
        blacklist.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}
