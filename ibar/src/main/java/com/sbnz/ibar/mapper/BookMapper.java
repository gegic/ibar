package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

	public BookDto toDto(Book entity) {
		BookDto dto = new BookDto();
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setQuantity(entity.getQuantity());
		dto.setCategory(entity.getCategory().getId());
		return dto;
	}

}
