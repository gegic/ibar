package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.model.Author;
import com.sbnz.ibar.model.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

	public BookDto toBookDto(Book entity) {
		BookDto dto = new BookDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setQuantity(entity.getQuantity());
		dto.setCategoryId(entity.getCategory().getId());
		dto.setCategoryName(entity.getCategory().getName());
		dto.setCover(entity.getCover());
		dto.setNumReviews(entity.getNumReviews());
		dto.setAverageRating(entity.getAverageRating());
		dto.setAuthorName(entity.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", ")));
		dto.setPdf(entity.getPdf());
		return dto;
	}

}
