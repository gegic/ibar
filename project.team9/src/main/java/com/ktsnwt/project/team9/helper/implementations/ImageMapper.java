package com.ktsnwt.project.team9.helper.implementations;

import org.springframework.stereotype.Component;
import com.ktsnwt.project.team9.dto.ImageDTO;
import com.ktsnwt.project.team9.helper.interfaces.ListMapper;
import com.ktsnwt.project.team9.model.Image;

@Component
public class ImageMapper extends ListMapper<Image, ImageDTO> {

	@Override
	public Image toEntity(ImageDTO dto) {
		return new Image(dto.getUrl());
	}

	@Override
	public ImageDTO toDto(Image entity) {
		return new ImageDTO(entity.getUrl());
	}
}
