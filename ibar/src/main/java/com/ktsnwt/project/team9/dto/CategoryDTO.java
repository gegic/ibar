package com.ktsnwt.project.team9.dto;

import javax.validation.constraints.NotEmpty;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

	private Long id;
	
	@NotEmpty(message = "Name cannot be null or empty.")
	private String name;
	
	@NotEmpty(message = "Discription cannot be null or empty.")
	private String description;
	
	private boolean active;
}
