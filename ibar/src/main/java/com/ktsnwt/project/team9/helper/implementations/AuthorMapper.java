package com.ktsnwt.project.team9.helper.implementations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.AuthorDTO;
import com.ktsnwt.project.team9.dto.response.AuthorResDTO;
import com.ktsnwt.project.team9.helper.interfaces.IMapper;
import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.Book;

@Component
public class AuthorMapper implements IMapper<Author, AuthorDTO> {

	@Override
	public Author toEntity(@Valid AuthorDTO dto) {
		return new Author(dto.getName(), dto.getDescription(), dto.getDateOfBirth(), dto.getDateOfDeath());
	}

	public AuthorResDTO toDTORes(Author entity) {
		return new AuthorResDTO(
				entity.getId(), entity.getName(), entity.getDescription(), entity.getImage(), entity.getAverageMark(),
				entity.getDateOfBirth(), entity.getDateOfDeath(), Optional.ofNullable(entity.getWrittenBooks())
						.orElse(new HashSet<>()).stream().map(this::transformBooksToId).collect(Collectors.toSet()),
				null);
	}

	public List<AuthorResDTO> toDTOResList(Iterable<Author> entities) {
		List<AuthorResDTO> dtos = new ArrayList<>();
		for (Author entity : entities) {
			dtos.add(toDTORes(entity));
		}
		return dtos;
	}

	@Override
	public AuthorDTO toDto(Author entity) {
		return null;
	}

	private Book transformIdsToBooks(Long id) {
		return new Book(id);
	}

	private Long transformBooksToId(Book book) {
		return book.getId();
	}
}
