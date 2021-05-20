package com.sbnz.ibar.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	@NotEmpty(message = "Name cannot be null or empty.")
	private String name;

	private String description;

	private long quantity;

	@NotNull(message = "Category cannot be null.")
	private Long category;

}