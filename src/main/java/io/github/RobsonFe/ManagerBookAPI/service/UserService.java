package io.github.RobsonFe.ManagerBookAPI.service;

import io.github.RobsonFe.ManagerBookAPI.dto.UserDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.entity.UserModel;
import io.github.RobsonFe.ManagerBookAPI.exception.UserNotFoundException;
import io.github.RobsonFe.ManagerBookAPI.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Injetando o PasswordEncoder

    // Criação do usuário com a senha encriptografada
    public MessageResponseDTO create(@Valid UserDTO userDTO) {
        // Criptografando a senha
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Construindo o modelo de usuário
        UserModel userModel = UserModel.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(encryptedPassword) // Usando a senha criptografada
                .build();

        // Salvando no banco de dados
        userRepository.save(userModel);

        return MessageResponseDTO.builder()
                .message("User created successfully with ID " + userModel.getId())
                .build();
    }

    public UserDTO findById(Long id) throws UserNotFoundException {
        UserModel userModel = verifyIfExists(id);
        return new UserDTO(userModel.getUsername(), userModel.getEmail(), userModel.getPassword(), null);
    }

    public Page<UserDTO> findAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).map(
                userModel -> new UserDTO(userModel.getUsername(), userModel.getEmail(), userModel.getPassword(), null)
        );
    }

    public MessageResponseDTO update(Long id, @Valid UserDTO userDTO) throws UserNotFoundException {
        verifyIfExists(id);

        // Criptografando a nova senha
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

        UserModel updatedUserModel = UserModel.builder()
                .id(id)
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(encryptedPassword) // Usando a senha criptografada na atualização
                .build();

        userRepository.save(updatedUserModel);

        return MessageResponseDTO.builder()
                .message("User updated successfully with ID " + id)
                .build();
    }

    public void delete(Long id) throws UserNotFoundException {
        verifyIfExists(id);
        userRepository.deleteById(id);
    }

    private UserModel verifyIfExists(Long id) throws UserNotFoundException {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return user.get();
    }
}
