package io.github.RobsonFe.ManagerBookAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.RobsonFe.ManagerBookAPI.dto.BookDTO;
import io.github.RobsonFe.ManagerBookAPI.dto.MessageResponseDTO;
import io.github.RobsonFe.ManagerBookAPI.entity.Book;
import io.github.RobsonFe.ManagerBookAPI.exception.BookNotFoundExeption;
import io.github.RobsonFe.ManagerBookAPI.mapper.BookMapper;
import io.github.RobsonFe.ManagerBookAPI.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper = BookMapper.INSTANCE;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public MessageResponseDTO<BookDTO> create(BookDTO bookDTO) {
        Book bookToSave = bookMapper.toModel(bookDTO);
        Book savedBook = bookRepository.save(bookToSave);
        BookDTO savedBookDTO = bookMapper.toDTO(savedBook);
        
        return MessageResponseDTO.<BookDTO>builder()
                .message("O livro foi criado com sucesso")
                .data(savedBookDTO)
                .build();
    }

    public MessageResponseDTO<BookDTO> findById(Long id) throws BookNotFoundExeption {
        Book book = verifyIfExists(id);
        BookDTO bookDTO = bookMapper.toDTO(book);
        
        return MessageResponseDTO.<BookDTO>builder()
                .message("Livro encontrado com sucesso")
                .data(bookDTO)
                .build();
    }

    public MessageResponseDTO<BookDTO> findByName(String name) throws BookNotFoundExeption {
        Book book = verifyIfName(name);
        BookDTO bookDTO = bookMapper.toDTO(book);
        
        return MessageResponseDTO.<BookDTO>builder()
                .message("Livro encontrado com sucesso")
                .data(bookDTO)
                .build();
    }

 public MessageResponseDTO<Page<BookDTO>> findAll(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Book> books = bookRepository.findAll(pageable);
    Page<BookDTO> bookDTOs = books.map(bookMapper::toDTO);
    
    return MessageResponseDTO.<Page<BookDTO>>builder()
            .message("Lista de livros retornada com sucesso")
            .data(bookDTOs)
            .build();
    }


    public MessageResponseDTO<BookDTO> update(Long id, BookDTO bookDTO) throws BookNotFoundExeption {
        verifyIfExists(id);
        Book bookToUpdate = bookMapper.toModel(bookDTO);
        bookToUpdate.setId(id);
        Book updatedBook = bookRepository.save(bookToUpdate);
        BookDTO updatedBookDTO = bookMapper.toDTO(updatedBook);
        
        return MessageResponseDTO.<BookDTO>builder()
                .message("Dados atualizados com sucesso")
                .data(updatedBookDTO)
                .build();
    }

    public MessageResponseDTO<String> delete(Long id) throws BookNotFoundExeption {
        verifyIfExists(id);
        bookRepository.deleteById(id);
        
        return MessageResponseDTO.<String>builder()
                .message("Livro deletado com sucesso")
                .data("ID do livro deletado: " + "com " + id)
                .build();
    }

    private Book verifyIfExists(Long id) throws BookNotFoundExeption {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundExeption(id));
    }

    private Book verifyIfName(String name) throws BookNotFoundExeption {
        return bookRepository.findByName(name)
                .orElseThrow(() -> new BookNotFoundExeption(name));
    }
}