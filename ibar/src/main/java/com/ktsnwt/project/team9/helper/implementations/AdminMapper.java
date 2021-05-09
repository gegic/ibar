package com.ktsnwt.project.team9.helper.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ktsnwt.project.team9.dto.AdminDTO;
import com.ktsnwt.project.team9.dto.response.UserResDTO;
import com.ktsnwt.project.team9.helper.interfaces.IMapper;
import com.ktsnwt.project.team9.model.Admin;

import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor
public class AdminMapper implements IMapper<Admin, AdminDTO> {

	@Override
	public Admin toEntity(AdminDTO dto) {
		return new Admin(dto.getUsername(), dto.getEmail(), dto.getFirstName(), dto.getLastName());
	}

	@Override
	public AdminDTO toDto(Admin entity) {
		return null;
	}
	
	public List<AdminDTO> toDTOList(Iterable<Admin> entities) {
		List<AdminDTO> dtos = new ArrayList<>();
		for(Admin entity : entities) {
			dtos.add(toDto(entity));
		}
		return dtos;
	}

	public UserResDTO toResDTO(Admin entity) {
		return new UserResDTO(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getPassword(), entity.getFirstName(), entity.getLastName());
	}
	
	public List<UserResDTO> toDTOResList(List<Admin> entities) {
		List<UserResDTO> dtos = new ArrayList<>();
		for(Admin entity : entities) {
			dtos.add(toResDTO(entity));
		}
		return dtos;
	}

}
