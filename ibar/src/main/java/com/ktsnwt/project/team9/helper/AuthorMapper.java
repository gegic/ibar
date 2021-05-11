package com.ktsnwt.project.team9.helper;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.AuthorDTO;
import com.ktsnwt.project.team9.model.Author;

@Component
public class AuthorMapper {

	public Author toEntity(@Valid AuthorDTO dto) {
		Author a = new Author();
		a.setName(dto.getName());
		a.setId(dto.getId());
		a.setAverageRating(dto.getAverageRating());
		a.setDateOfBirth(dto.getDateOfBirth());
		a.setDateOfDeath(dto.getDateOfDeath());
		return a;
	}

	public AuthorDTO toDto(Author entity) {
		AuthorDTO dto = new AuthorDTO();
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setDateOfBirth(entity.getDateOfBirth());
		dto.setDateOfDeath(entity.getDateOfDeath());

		return dto;
	}
}
