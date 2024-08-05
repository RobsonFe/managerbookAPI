package io.github.RobsonFe.ManagerBookAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundExeption extends Exception {

     public BookNotFoundExeption(String message) {
        super(message);
    }

    public BookNotFoundExeption(Long id) {
        super(String.format("Book with id not found"));
    }
}
