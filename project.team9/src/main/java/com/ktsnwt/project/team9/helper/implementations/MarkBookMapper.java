package com.ktsnwt.project.team9.helper.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.MarkBookDTO;
import com.ktsnwt.project.team9.helper.interfaces.IMapper;
import com.ktsnwt.project.team9.model.Book;
import com.ktsnwt.project.team9.model.MarkBook;
import com.ktsnwt.project.team9.model.RegisteredUser;

@Component
public class MarkBookMapper implements IMapper<MarkBook, MarkBookDTO> {

	@Override
	public MarkBook toEntity(MarkBookDTO dto) {
		return new MarkBook(dto.getId(), dto.getValue(), new RegisteredUser((long) 0), new Book(dto.getBook()));
	}

	public MarkBook dtoToEntity(MarkBookDTO dto, Long id) {
		return new MarkBook(dto.getId(), dto.getValue(), new RegisteredUser(id), new Book(dto.getBook()));
	}

	@Override
	public MarkBookDTO toDto(MarkBook entity) {
		return new MarkBookDTO(entity.getId(), entity.getValue(), entity.getBook().getId());
	}

	public List<MarkBookDTO> toDTOList(Iterable<MarkBook> entities) {
		List<MarkBookDTO> dtos = new ArrayList<>();
		for (MarkBook entity : entities) {
			dtos.add(toDto(entity));
		}
		return dtos;
	}
}
