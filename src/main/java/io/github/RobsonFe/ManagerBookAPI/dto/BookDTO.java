package io.github.RobsonFe.ManagerBookAPI.dto;

import io.github.RobsonFe.ManagerBookAPI.entity.Author;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookDTO {
        Long id;
        @NotBlank
        @Size(max = 200)
        String name;
        @NotNull
        Integer pages;
        @NotNull
        Integer chapters;
        @NotBlank
        @Size(max = 17)
        @Pattern(regexp = "^(?:ISBN(?:-10)?:?\\s)?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
                message = "ISBN format must be a valid format")
        String isbn;
        @NotBlank
        @Size(max = 200)
        String publisherName;
        @Valid
        @NotNull
        AuthorDTO author;
}
