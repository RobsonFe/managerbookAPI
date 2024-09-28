package io.github.RobsonFe.ManagerBookAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.RobsonFe.ManagerBookAPI.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
