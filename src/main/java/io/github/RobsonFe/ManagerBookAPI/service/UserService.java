package io.github.RobsonFe.ManagerBookAPI.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.UserDTO;
import io.github.RobsonFe.ManagerBookAPI.entity.User;
import io.github.RobsonFe.ManagerBookAPI.exception.UserNotFoundException;
import io.github.RobsonFe.ManagerBookAPI.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Serviço responsável pelas operações CRUD de usuários, incluindo a criação,
 * atualização, busca e exclusão de usuários, além da verificação de existência
 * de um usuário por ID.
 */
@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    /**
     * Cria um novo usuário no sistema.
     *
     * @param userDTO Dados do usuário a ser criado.
     * @return Mensagem de resposta contendo detalhes da operação.
     */
    public MessageResponseDTO<UserDTO> create(@Valid UserDTO userDTO) {

        // Verifica se as senhas informadas são iguais
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("As senhas não são iguais!");
        }

        // Verifica se o email já existe no sistema
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("O E-mail informado já existe no sistema");
        }

        // Cria uma nova instância de usuário e codifica a senha
        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        // Salva o novo usuário no repositório
        userRepository.save(user);

        // Retorna uma mensagem de sucesso com os dados do usuário
        return MessageResponseDTO.<UserDTO>builder()
                .message("Usuário Criado com Sucesso! " + user.getId())
                .data(userDTO)
                .build();
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id ID do usuário a ser buscado.
     * @return Dados do usuário encontrado.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    public UserDTO findById(Long id) throws UserNotFoundException {
        User user = verifyIfExists(id);
        // Retorna um DTO contendo os dados do usuário
        return new UserDTO(user.getUsername(), user.getEmail(), user.getPassword(), null);
    }

    /**
     * Lista todos os usuários com paginação.
     *
     * @param page Página atual da listagem.
     * @param size Quantidade de usuários por página.
     * @return Uma página de usuários convertidos para DTO.
     */
    public Page<UserDTO> findAll(int page, int size) {
        // Mapeia cada usuário para um DTO
        return userRepository.findAll(PageRequest.of(page, size)).map(
                user -> new UserDTO(user.getUsername(), user.getEmail(), user.getPassword(), null)
        );
    }

    /**
     * Atualiza um usuário existente.
     *
     * @param id ID do usuário a ser atualizado.
     * @param userDTO Dados atualizados do usuário.
     * @return Mensagem de resposta contendo detalhes da operação.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    public MessageResponseDTO<UserDTO> update(Long id, @Valid UserDTO userDTO) throws UserNotFoundException {
        // Verifica se o usuário existe
        verifyIfExists(id);

        // Atualiza os dados do usuário
        User updatedUser = User.builder()
                .id(id)
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword()) // Aqui deveria codificar a senha novamente
                .build();

        // Salva o usuário atualizado no repositório
        userRepository.save(updatedUser);

        // Retorna uma mensagem de sucesso com os dados atualizados
        return MessageResponseDTO.<UserDTO>builder()
                .message("User updated successfully with ID " + id)
                .data(userDTO)
                .build();
    }

    /**
     * Exclui um usuário pelo ID.
     *
     * @param id ID do usuário a ser excluído.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    public void delete(Long id) throws UserNotFoundException {
        verifyIfExists(id);
        userRepository.deleteById(id);
    }

    /**
     * Verifica se um usuário com o ID fornecido existe.
     *
     * @param id ID do usuário a ser verificado.
     * @return O usuário, caso exista.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    private User verifyIfExists(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        // Lança uma exceção se o usuário não for encontrado
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return user.get();
    }
}
