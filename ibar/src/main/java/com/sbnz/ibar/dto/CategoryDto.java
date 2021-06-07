package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

	private UUID id;
	
	@NotEmpty(message = "Name cannot be null or empty.")
	private String name;
	
	@NotEmpty(message = "Description cannot be null or empty.")
	private String description;
	
	private boolean active;
}
