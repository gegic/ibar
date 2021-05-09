package com.ktsnwt.project.team9.helper.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.MarkAuthorDTO;
import com.ktsnwt.project.team9.helper.interfaces.IMapper;
import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.MarkAuthor;
import com.ktsnwt.project.team9.model.RegisteredUser;

@Component
public class MarkAuthorMapper implements IMapper<MarkAuthor, MarkAuthorDTO> {

	@Override
	public MarkAuthor toEntity(MarkAuthorDTO dto) {
		return new MarkAuthor(dto.getId(), dto.getValue(), new RegisteredUser((long) 0), new Author(dto.getAuthor()));
	}

	public MarkAuthor dtoToEntity(MarkAuthorDTO dto, Long id) {
		return new MarkAuthor(dto.getId(), dto.getValue(), new RegisteredUser(id), new Author(dto.getAuthor()));
	}

	@Override
	public MarkAuthorDTO toDto(MarkAuthor entity) {
		return new MarkAuthorDTO(entity.getId(), entity.getValue(), entity.getAuthor().getId());
	}

	public List<MarkAuthorDTO> toDTOList(Iterable<MarkAuthor> entities) {
		List<MarkAuthorDTO> dtos = new ArrayList<>();
		for (MarkAuthor entity : entities) {
			dtos.add(toDto(entity));
		}
		return dtos;
	}
}
