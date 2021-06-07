package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.AuthorDto;
import com.sbnz.ibar.model.Author;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class AuthorMapper {

	public Author toEntity(@Valid AuthorDto dto) {
		Author a = new Author();
		a.setName(dto.getName());
		a.setId(dto.getId());
		a.setAverageRating(dto.getAverageRating());
		a.setDateOfBirth(dto.getDateOfBirth());
		a.setDateOfDeath(dto.getDateOfDeath());
		return a;
	}

	public AuthorDto toDto(Author entity) {
		AuthorDto dto = new AuthorDto();
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setDateOfBirth(entity.getDateOfBirth());
		dto.setDateOfDeath(entity.getDateOfDeath());
		dto.setAverageRating(entity.getAverageRating());

		return dto;
	}
}
