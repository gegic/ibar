package com.ktsnwt.project.team9.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class BookDTO {

	@NotEmpty(message = "Name cannot be null or empty.")
	private String name;

	private String description;

	private long quantity;

	@NotNull(message = "Category cannot be null.")
	private Long category;

}
