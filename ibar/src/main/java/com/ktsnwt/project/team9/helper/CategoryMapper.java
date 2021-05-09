package com.ktsnwt.project.team9.helper;

import org.springframework.stereotype.Component;
import com.ktsnwt.project.team9.dto.CategoryDTO;
import com.ktsnwt.project.team9.model.Category;

@Component
public class CategoryMapper {

	public CategoryDTO toDto(Category entity) {
		return new CategoryDTO(entity.getId(), entity.getName(), entity.getDescription(), entity.isActive());
	}
}
