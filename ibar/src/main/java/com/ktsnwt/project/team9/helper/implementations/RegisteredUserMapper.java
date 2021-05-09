package com.ktsnwt.project.team9.helper.implementations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.RegisteredUserDTO;
import com.ktsnwt.project.team9.dto.UserDTO;
import com.ktsnwt.project.team9.dto.response.UserResDTO;
import com.ktsnwt.project.team9.helper.interfaces.IMapper;
import com.ktsnwt.project.team9.model.Comment;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.model.RegisteredUser;

@Component
public class RegisteredUserMapper implements IMapper<RegisteredUser, RegisteredUserDTO> {

	@Override
	public RegisteredUser toEntity(RegisteredUserDTO user) {
		return new RegisteredUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstName(),
				user.getLastName());
	}

	public RegisteredUser toEntity(UserDTO user) {
		return new RegisteredUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstName(),
				user.getLastName());
	}

	@Override
	public RegisteredUserDTO toDto(RegisteredUser entity) {
		return new RegisteredUserDTO(entity.getUsername(), entity.getEmail(), entity.getPassword(),
				entity.getFirstName(), entity.getLastName(),
				Optional.ofNullable(entity.getMarksAuthor()).orElse(new HashSet<>()).stream()
						.map(this::transformMarkToId).collect(Collectors.toSet()),
				Optional.ofNullable(entity.getMarksBook()).orElse(new HashSet<>()).stream().map(this::transformMarkToId)
						.collect(Collectors.toSet()),
				Optional.ofNullable(entity.getCommentsAuthor()).orElse(new HashSet<>()).stream()
						.map(this::transformCommentToId).collect(Collectors.toSet()),
				Optional.ofNullable(entity.getCommentsBook()).orElse(new HashSet<>()).stream()
						.map(this::transformCommentToId).collect(Collectors.toSet()));
	}

	public UserResDTO toResDTO(RegisteredUser entity) {
		return new UserResDTO(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getPassword(),
				entity.getFirstName(), entity.getLastName());
	}

	public List<UserResDTO> toResDTOList(Iterable<RegisteredUser> entities) {
		List<UserResDTO> dtos = new ArrayList<>();
		for (RegisteredUser entity : entities) {
			dtos.add(toResDTO(entity));
		}
		return dtos;
	}

	public List<UserResDTO> toDTOResList(List<RegisteredUser> entities) {
		List<UserResDTO> dtos = new ArrayList<>();
		for (RegisteredUser entity : entities) {
			dtos.add(toResDTO(entity));
		}
		return dtos;
	}

	private Long transformCommentToId(Comment entity) {
		return entity.getId();
	}

	private Long transformMarkToId(Mark entity) {
		return entity.getId();
	}

}
