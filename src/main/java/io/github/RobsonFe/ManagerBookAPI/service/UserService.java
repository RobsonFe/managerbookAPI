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

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public MessageResponseDTO<UserDTO> create(@Valid UserDTO userDTO) {

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("As senhas não são iguais!");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("O E-mail informado já existe no sistema");
        }

        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
        userRepository.save(user);

        return MessageResponseDTO.<UserDTO>builder()
                .message("Usuário Criado com Sucesso! " + user.getId())
                .data(userDTO)
                .build();
    }

    public UserDTO findById(Long id) throws UserNotFoundException {
        User user = verifyIfExists(id);
        return new UserDTO(user.getUsername(), user.getEmail(), user.getPassword(), null);
    }

    public Page<UserDTO> findAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).map(
                user -> new UserDTO(user.getUsername(), user.getEmail(), user.getPassword(), null)
        );
    }

    public MessageResponseDTO<UserDTO> update(Long id, @Valid UserDTO userDTO) throws UserNotFoundException {
        verifyIfExists(id);

        User updatedUser = User.builder()
                .id(id)
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

        userRepository.save(updatedUser);

        return MessageResponseDTO.<UserDTO>builder()
                .message("User updated successfully with ID " + id)
                .data(userDTO)
                .build();
    }

    public void delete(Long id) throws UserNotFoundException {
        verifyIfExists(id);
        userRepository.deleteById(id);
    }

    private User verifyIfExists(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return user.get();
    }
}
