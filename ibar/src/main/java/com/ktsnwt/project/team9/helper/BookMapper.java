package com.ktsnwt.project.team9.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.ktsnwt.project.team9.model.enums.BookType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.BookDTO;
import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.Book;
import com.ktsnwt.project.team9.model.Category;

@Component
public class BookMapper {

	public BookDTO toDto(Book entity) {
		BookDTO dto = new BookDTO();
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setQuantity(entity.getQuantity());
		dto.setCategory(entity.getCategory().getId());
		return dto;
	}

}
