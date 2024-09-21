package io.github.RobsonFe.ManagerBookAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Objeto que representa a resposta com uma mensagem")
public class MessageResponseDTO<T> {

    @Schema(description = "Mensagem de resposta", example = "Dado retornado com sucesso")
    private String message;

    @Schema(description = "Dados retornados pela operação")
    private T data;

    // Construtor adicional para casos onde apenas a mensagem é necessária
    public MessageResponseDTO(String message) {
        this.message = message;
    }
}
