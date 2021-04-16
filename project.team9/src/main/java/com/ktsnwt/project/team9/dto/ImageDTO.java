package com.ktsnwt.project.team9.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ImageDTO {
	
	@NotBlank(message = "URL can't be empty")
	private String url;

	public ImageDTO() {
		// TODO Auto-generated constructor stub
	}
}