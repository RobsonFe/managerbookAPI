package io.github.RobsonFe.ManagerBookAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object para a criação de um livro")
public class BookDTO {
        Long id;

        @NotBlank
        @Size(max = 200)
        @Schema(description = "Nome do livro", example = "Harry Potter e a Câmara Secreta")
        String name;

        @NotNull
        @Schema(description = "Número de páginas do livro", example = "251")
        Integer pages;

        @NotNull
        @Schema(description = "Número de capítulos do livro", example = "18")
        Integer chapters;

        @NotBlank
        @Size(max = 17)
        @Pattern(regexp = "^(?:ISBN(?:-10)?:?\\s)?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
                message = "ISBN format must be a valid format")
        @Schema(description = "ISBN do livro", example = "978-3-16-148411-7")
        String isbn;

        @NotBlank
        @Size(max = 200)
        @Schema(description = "Nome da editora do livro", example = "Bloomsbury")
        String publisherName;

        @Valid
        @NotNull
        @Schema(description = "Autor do livro")
        AuthorDTO author;
}

