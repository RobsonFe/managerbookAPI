package io.github.RobsonFe.ManagerBookAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthorDTO {

        Long id;
        @NotBlank @Size(max = 200)
        String name;
        @NotNull @Size(max = 100)
        Integer age;
}
