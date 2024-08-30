package io.github.RobsonFe.ManagerBookAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um usuário não é encontrado.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    /**
     * Construtor que recebe uma mensagem personalizada.
     *
     * @param message Mensagem personalizada
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Construtor que usa um ID para criar uma mensagem padrão.
     *
     * @param id ID do usuário não encontrado
     */
    public UserNotFoundException(Long id) {
        super(String.format("User with ID %d not found", id));
    }
}
