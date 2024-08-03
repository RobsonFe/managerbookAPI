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
@Schema(description = "Entidade representando um livro")
public class Book {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do livro", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nome do livro", example = "Harry Potter e a Pedra Filosofal")
    private String name;

    @Column(nullable = false)
    @Schema(description = "Número de páginas do livro", example = "223")
    private Integer pages;

    @Column(nullable = false)
    @Schema(description = "Número de capítulos do livro", example = "17")
    private Integer chapters;

    @Column(nullable = false)
    @Schema(description = "ISBN do livro", example = "978-3-16-148410-0")
    private String isbn;

    @Column(nullable = false, name = "publisher_name", unique = true)
    @Schema(description = "Nome da editora do livro", example = "Bloomsbury")
    private String publisherName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "author_id")
    @Schema(description = "Autor do livro")
    private Author author;

}
