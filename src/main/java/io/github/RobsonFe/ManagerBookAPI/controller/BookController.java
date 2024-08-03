package io.github.RobsonFe.ManagerBookAPI.controller;

import io.github.RobsonFe.ManagerBookAPI.dto.BookDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.exception.BookNotFoundExeption;
import io.github.RobsonFe.ManagerBookAPI.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "API para gerenciamento de livros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Cria um novo livro", description = "Adiciona um novo livro à biblioteca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro no servidor",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("criar/")
    public MessageResponseDTO create(@RequestBody @Valid BookDTO bookDTO){
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
    @GetMapping("buscar/{id}")
    public BookDTO findById(@PathVariable Long id) throws BookNotFoundExeption {
        return bookService.findById(id);
    }

    @Operation(summary = "Lista todos os livros", description = "Retorna uma lista de todos os livros na biblioteca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro no servidor",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("listar")
    public List<BookDTO> findAll() {
        return bookService.findAll();
    }

    @Operation(summary = "Atualiza um livro", description = "Atualiza as informações de um livro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro no servidor",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("atualizar/{id}")
    public MessageResponseDTO update(@PathVariable Long id, @RequestBody @Valid BookDTO bookDTO) throws BookNotFoundExeption {
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
    @DeleteMapping("deletar/{id}")
    public void delete(@PathVariable Long id) throws BookNotFoundExeption {
        bookService.delete(id);
    }
}
