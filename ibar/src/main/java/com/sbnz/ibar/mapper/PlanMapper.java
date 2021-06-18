package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.PlanDto;
import com.sbnz.ibar.model.Category;
import com.sbnz.ibar.model.Plan;
import com.sbnz.ibar.model.Rank;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PlanMapper {

	public PlanDto toDto(Plan entity) {
		PlanDto dto = new PlanDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setPrice(entity.getPrice());
		dto.setDescription(entity.getDescription());
		dto.setDayDuration(entity.getDayDuration());
		dto.setCategoryNames(entity.getCategories().stream().map(Category::getName).collect(Collectors.toSet()));
		dto.setCategoryIds(entity.getCategories().stream().map(Category::getId).collect(Collectors.toSet()));
		dto.setRankName(entity.getRank().getName());
		dto.setRankId(entity.getRank().getId());
		return dto;
	}
}
