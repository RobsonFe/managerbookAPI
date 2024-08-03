package io.github.RobsonFe.ManagerBookAPI.mapper;

import io.github.RobsonFe.ManagerBookAPI.dto.AuthorDTO;
import io.github.RobsonFe.ManagerBookAPI.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO toDTO(Author author);

    Author toModel(AuthorDTO authorDTO);
}
