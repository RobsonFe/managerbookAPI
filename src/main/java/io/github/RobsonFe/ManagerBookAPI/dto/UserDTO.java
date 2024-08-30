package io.github.RobsonFe.ManagerBookAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object para o registro e login de usuário")
public class UserDTO {

    @NotBlank(message = "Username is required")
    @Schema(description = "Nome do usuário", example = "robsonfe")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "E-mail do usuário", example = "robson.fe@email.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    @Schema(description = "Senha do usuário", example = "********")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    @Size(min = 8, message = "Password confirmation should be at least 8 characters long")
    @Schema(description = "Confirmação da senha do usuário", example = "********")
    private String confirmPassword;
}
