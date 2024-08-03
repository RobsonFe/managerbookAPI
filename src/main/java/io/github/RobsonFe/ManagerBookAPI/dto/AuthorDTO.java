package io.github.RobsonFe.ManagerBookAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDTO {

        Long id;
        @NotBlank @Size(max = 200)
        @Schema(description = "Nome do autor", example = "J. K. Rowling")
        String name;

        @NotNull @Size(max = 100)
        @Schema(description = "Idade do autor", example = "59")
        Integer age;
}
