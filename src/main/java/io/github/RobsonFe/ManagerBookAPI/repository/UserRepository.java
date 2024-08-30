package io.github.RobsonFe.ManagerBookAPI.repository;

import io.github.RobsonFe.ManagerBookAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
