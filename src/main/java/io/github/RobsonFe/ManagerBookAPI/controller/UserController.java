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

import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.UserDTO;
import io.github.RobsonFe.ManagerBookAPI.exception.UserNotFoundException;
import io.github.RobsonFe.ManagerBookAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API para gerenciamento de usuários")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Cria um novo usuário", description = "Adiciona um novo usuário ao sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/criar")
    public MessageResponseDTO<UserDTO> create(@RequestBody @Valid UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @Operation(summary = "Busca um usuário pelo ID", description = "Retorna os detalhes de um usuário específico com base no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/buscar/{id}")
    public UserDTO findById(@PathVariable Long id) throws UserNotFoundException {
        return userService.findById(id);
    }

    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista de todos os usuários")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/listar")
    public Page<UserDTO> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.findAll(page, size);
    }

    @Operation(summary = "Atualiza um usuário", description = "Atualiza as informações de um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/atualizar/{id}")
    public MessageResponseDTO<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) throws UserNotFoundException {
        return userService.update(id, userDTO);
    }

    @Operation(summary = "Deleta um usuário", description = "Remove um usuário do sistema pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/deletar/{id}")
    public void delete(@PathVariable Long id) throws UserNotFoundException {
        userService.delete(id);
    }
}
