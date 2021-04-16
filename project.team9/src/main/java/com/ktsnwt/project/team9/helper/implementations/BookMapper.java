package com.ktsnwt.project.team9.helper.implementations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.ktsnwt.project.team9.dto.BookDTO;
import com.ktsnwt.project.team9.dto.response.BookResDTO;
import com.ktsnwt.project.team9.helper.interfaces.IMapper;
import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.Book;
import com.ktsnwt.project.team9.model.Category;

public class BookMapper implements IMapper<Book, BookDTO> {

	private CategoryMapper categoryMapper;

	@Override
	public Book toEntity(@Valid BookDTO dto) {
		return new Book(dto.getName(), dto.getDescription(), new Category(dto.getCategory()), dto.getPrice(),
				dto.getQuantity(),
				dto.getWrittenBy().stream().map((id) -> this.transformIdsToAuthors(id)).collect(Collectors.toSet()));
	}

	public BookResDTO toDTORes(Book entity) {
		return new BookResDTO(entity.getId(), entity.getName(), entity.getDescription(), entity.getImage().getUrl(),
				entity.getAverageMark(), entity.getQuantity(), entity.getPrice(),
				categoryMapper.toDto(entity.getCategory()), Optional.ofNullable(entity.getWrittenBy())
						.orElse(new HashSet<>()).stream().map(this::transformAuthorsToId).collect(Collectors.toSet()),
				null);
	}

	public List<BookResDTO> toDTOResList(Iterable<Book> entities) {
		List<BookResDTO> dtos = new ArrayList<>();
		for (Book entity : entities) {
			dtos.add(toDTORes(entity));
		}
		return dtos;
	}

	@Override
	public BookDTO toDto(Book entity) {
		return null;
	}

	private Author transformIdsToAuthors(Long id) {
		return new Author(id);
	}

	private Long transformAuthorsToId(Author author) {
		return author.getId();
	}
}
