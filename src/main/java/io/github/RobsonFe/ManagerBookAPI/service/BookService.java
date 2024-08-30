package io.github.RobsonFe.ManagerBookAPI.service;

import io.github.RobsonFe.ManagerBookAPI.dto.BookDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.entity.Book;
import io.github.RobsonFe.ManagerBookAPI.exception.BookNotFoundExeption;
import io.github.RobsonFe.ManagerBookAPI.mapper.BookMapper;
import io.github.RobsonFe.ManagerBookAPI.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .message("O livro foi criado com sucesso: " + savedBook)
                .build();
    }

    public BookDTO findById(Long id) throws BookNotFoundExeption {
        Book book = verifyIfExists(id);
        return bookMapper.toDTO(book);
    }

    public BookDTO findByName(String name) throws BookNotFoundExeption {
        Book book = verifyIfName(name);
        return bookMapper.toDTO(book);
    }

    public Page<BookDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::toDTO); 
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

    private Book verifyIfName(String name) throws BookNotFoundExeption {
    return bookRepository.findByName(name)
            .orElseThrow(() -> new BookNotFoundExeption(name));
}

}

