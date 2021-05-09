package com.ktsnwt.project.team9.helper.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.CommentBookDTO;
import com.ktsnwt.project.team9.dto.response.CommentBookResDTO;
import com.ktsnwt.project.team9.helper.interfaces.IMapper;
import com.ktsnwt.project.team9.model.Book;
import com.ktsnwt.project.team9.model.CommentBook;
import com.ktsnwt.project.team9.model.RegisteredUser;

@Component
public class CommentBookMapper implements IMapper<CommentBook, CommentBookDTO> {

	@Override
	public CommentBook toEntity(CommentBookDTO dto) {
		return null;
	}

	@Override
	public CommentBookDTO toDto(CommentBook entity) {
		return null;
	}

	public List<CommentBookDTO> toDTOList(Iterable<CommentBook> entities) {
		List<CommentBookDTO> dtos = new ArrayList<>();
		for (CommentBook entity : entities) {
			dtos.add(toDto(entity));
		}
		return dtos;
	}

	public CommentBookResDTO toResDTO(CommentBook entity) {
		return new CommentBookResDTO(entity.getId(), entity.getDate(), entity.getWrittenBy().getUsername(),
				entity.getBook().getName(), entity.getText());
	}

	public List<CommentBookResDTO> toResDTOList(List<CommentBook> entities) {
		List<CommentBookResDTO> dtos = new ArrayList<>();
		for (CommentBook entity : entities) {
			dtos.add(toResDTO(entity));
		}
		return dtos;
	}

	public CommentBook dtoToEntity(CommentBookDTO dto, Long authorId) {
		return new CommentBook(new RegisteredUser(authorId), new Book(dto.getBook()), dto.getText(),
				(new Date()).getTime());
	}
}
