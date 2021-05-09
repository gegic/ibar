package com.ktsnwt.project.team9.helper;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.AuthorDTO;
import com.ktsnwt.project.team9.model.Author;

@Component
public class AuthorMapper {

	public Author toEntity(@Valid AuthorDTO dto) {
		return new Author(dto.getName(), dto.getDescription(), dto.getDateOfBirth(), dto.getDateOfDeath());
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
