package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.AuthorDto;
import com.sbnz.ibar.model.Author;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author entity) {
        AuthorDto dto = new AuthorDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDateOfBirth(Date.from(entity.getDateOfBirth()));
        dto.setDateOfDeath(Date.from(entity.getDateOfDeath()));
        dto.setAverageRating(entity.getAverageRating());
        dto.setImage(entity.getImage());

        return dto;
    }
}
