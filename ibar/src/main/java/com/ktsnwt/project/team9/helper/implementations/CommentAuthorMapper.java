package com.ktsnwt.project.team9.helper.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.CommentAuthorDTO;
import com.ktsnwt.project.team9.dto.response.CommentAuthorResDTO;
import com.ktsnwt.project.team9.helper.interfaces.IMapper;
import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.CommentAuthor;
import com.ktsnwt.project.team9.model.RegisteredUser;

@Component
public class CommentAuthorMapper implements IMapper<CommentAuthor, CommentAuthorDTO> {

	@Override
	public CommentAuthor toEntity(CommentAuthorDTO dto) {
		return null;
	}

	@Override
	public CommentAuthorDTO toDto(CommentAuthor entity) {
		return null;
	}

	public List<CommentAuthorDTO> toDTOList(Iterable<CommentAuthor> entities) {
		List<CommentAuthorDTO> dtos = new ArrayList<>();
		for (CommentAuthor entity : entities) {
			dtos.add(toDto(entity));
		}
		return dtos;
	}

	public CommentAuthorResDTO toResDTO(CommentAuthor entity) {
		return new CommentAuthorResDTO(entity.getId(), entity.getDate(), entity.getWrittenBy().getUsername(),
				entity.getAuthor().getName(), entity.getText());
	}

	public List<CommentAuthorResDTO> toResDTOList(List<CommentAuthor> entities) {
		List<CommentAuthorResDTO> dtos = new ArrayList<>();
		for (CommentAuthor entity : entities) {
			dtos.add(toResDTO(entity));
		}
		return dtos;
	}

	public CommentAuthor dtoToEntity(CommentAuthorDTO dto, Long authorId) {
		return new CommentAuthor(new RegisteredUser(authorId), new Author(dto.getAuthor()), dto.getText(),
				(new Date()).getTime());
	}
}
