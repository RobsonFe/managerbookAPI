package io.github.RobsonFe.ManagerBookAPI.repository;

import io.github.RobsonFe.ManagerBookAPI.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
    Optional<UserModel> findByUsername(String username);
}
