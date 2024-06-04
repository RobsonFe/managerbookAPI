package io.github.RobsonFe.ManagerBookAPI.controller;
import io.github.RobsonFe.ManagerBookAPI.dto.BookDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.exception.BookNotFoundExeption;
import io.github.RobsonFe.ManagerBookAPI.repository.BookRepository;
import io.github.RobsonFe.ManagerBookAPI.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/save")
    public MessageResponseDTO create(@RequestBody @Valid BookDTO bookDTO){
        return bookService.create(bookDTO);
    }

    @GetMapping("/search/{id}")
    public BookDTO findById(@PathVariable Long id) throws BookNotFoundExeption {
        return bookService.findById(id);
    }

}
