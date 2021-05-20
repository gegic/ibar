package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.CategoryDto;
import com.sbnz.ibar.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

	public CategoryDto toDto(Category entity) {
		return new CategoryDto(entity.getId(), entity.getName(), entity.getDescription(), entity.isActive());
	}
}
