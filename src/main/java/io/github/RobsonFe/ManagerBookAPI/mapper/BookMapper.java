package io.github.RobsonFe.ManagerBookAPI.mapper;

import io.github.RobsonFe.ManagerBookAPI.dto.BookDTO;
import io.github.RobsonFe.ManagerBookAPI.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "author", source = "author") // Mapeia explicitamente o autor
    BookDTO toDTO(Book book);

    @Mapping(target = "author", source = "author") // Mapeia explicitamente o autor
    Book toModel(BookDTO bookDTO);
}
