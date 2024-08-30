package io.github.RobsonFe.ManagerBookAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object para login de usuário")
public class UserLoginDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "E-mail do usuário", example = "robson.fe@email.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Senha do usuário", example = "********")
    private String password;
}
