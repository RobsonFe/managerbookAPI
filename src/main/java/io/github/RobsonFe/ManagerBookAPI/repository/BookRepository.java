package io.github.RobsonFe.ManagerBookAPI.repository;

import io.github.RobsonFe.ManagerBookAPI.entity.Book;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByName(String name);
}
