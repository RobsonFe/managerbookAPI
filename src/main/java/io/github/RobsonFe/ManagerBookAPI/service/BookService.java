package io.github.RobsonFe.ManagerBookAPI.service;

import io.github.RobsonFe.ManagerBookAPI.dto.BookDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.entity.Book;
import io.github.RobsonFe.ManagerBookAPI.exception.BookNotFoundExeption;
import io.github.RobsonFe.ManagerBookAPI.mapper.BookMapper;
import io.github.RobsonFe.ManagerBookAPI.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper = BookMapper.INSTANCE;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public MessageResponseDTO create(BookDTO bookDTO) {
        Book bookToSave = bookMapper.toModel(bookDTO);
        Book savedBook = bookRepository.save(bookToSave);
        return MessageResponseDTO.builder()
                .message("Book created com ID " + savedBook.getId())
                .build();
    }

    public BookDTO findById(Long id) throws BookNotFoundExeption {
        Book book = verifyIfExists(id);
        return bookMapper.toDTO(book);
    }

    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MessageResponseDTO update(Long id, BookDTO bookDTO) throws BookNotFoundExeption {
        verifyIfExists(id);
        Book bookToUpdate = bookMapper.toModel(bookDTO);
        bookToUpdate.setId(id);
        Book updatedBook = bookRepository.save(bookToUpdate);
        return MessageResponseDTO.builder()
                .message("Book atualizado com ID " + updatedBook.getId())
                .build();
    }

    public void delete(Long id) throws BookNotFoundExeption {
        verifyIfExists(id);
        bookRepository.deleteById(id);
    }

    private Book verifyIfExists(Long id) throws BookNotFoundExeption {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundExeption(id));
    }
}

