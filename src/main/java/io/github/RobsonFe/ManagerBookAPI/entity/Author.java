package io.github.RobsonFe.ManagerBookAPI.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Schema(description = "Entidade representando o Autor")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do Autor", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nome do autor", example = "J. K. Rowling")
    private String name;

    @Column(nullable = false)
    @Schema(description = "Idade do autor", example = "59")
    private Integer age;

}
