package io.github.RobsonFe.ManagerBookAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Objeto que representa a resposta com uma mensagem")
public class MessageResponseDTO {

    @Schema(description = "Mensagem de resposta", example = "Livro criado com sucesso")
    private String message;
}
