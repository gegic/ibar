package com.ktsnwt.project.team9.helper.implementations;

import org.springframework.stereotype.Component;
import com.ktsnwt.project.team9.dto.CategoryDTO;
import com.ktsnwt.project.team9.helper.interfaces.ListMapper;
import com.ktsnwt.project.team9.model.Category;

@Component
public class CategoryMapper extends ListMapper<Category, CategoryDTO> {

	@Override
	public Category toEntity(CategoryDTO dto) {
		return new Category(dto.getId(), dto.getName(), dto.getDescription(), dto.isActive());
	}

	@Override
	public CategoryDTO toDto(Category entity) {
		return new CategoryDTO(entity.getId(), entity.getName(), entity.getDescription(), entity.isActive());
	}
}
