package io.github.RobsonFe.ManagerBookAPI.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.RobsonFe.ManagerBookAPI.dto.BookDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.exception.BookNotFoundExeption;
import io.github.RobsonFe.ManagerBookAPI.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Books", description = "API para gerenciamento de livros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Cria um novo livro", description = "Adiciona um novo livro à biblioteca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Livro criado com sucesso",
                content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/criar")
    public MessageResponseDTO<BookDTO> create(@RequestBody @Valid BookDTO bookDTO) {
        return bookService.create(bookDTO);
    }

    @Operation(summary = "Busca um livro pelo ID", description = "Retorna os detalhes de um livro específico com base no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro encontrado",
                content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/buscar/{id}")
    public BookDTO findById(@PathVariable Long id) throws BookNotFoundExeption {
        return bookService.findById(id).getData();
    }

    @Operation(summary = "Busca um livro pelo Nome", description = "Retorna os detalhes de um livro específico com base no Nome fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro encontrado",
                content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/buscar/nome/{name}")
    public BookDTO findByName(@PathVariable String name) throws BookNotFoundExeption {
        return bookService.findByName(name).getData();
    }

    @Operation(summary = "Lista todos os livros", description = "Retorna uma lista de todos os livros na biblioteca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso",
                content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/listar")
    public MessageResponseDTO<Page<BookDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return bookService.findAll(page, size);
    }

    @Operation(summary = "Atualiza um livro", description = "Atualiza as informações de um livro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/atualizar/{id}")
    public MessageResponseDTO<BookDTO> update(@PathVariable Long id, @RequestBody @Valid BookDTO bookDTO) throws BookNotFoundExeption {
        return bookService.update(id, bookDTO);
    }

    @Operation(summary = "Deleta um livro", description = "Remove um livro da biblioteca pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/deletar/{id}")
    public MessageResponseDTO<String> delete(@PathVariable Long id) throws BookNotFoundExeption {
        return bookService.delete(id);
    }
}
