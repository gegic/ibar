package com.ktsnwt.project.team9.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookDTO {

	@NotEmpty(message = "Name cannot be null or empty.")
	private String name;

	private String description;

	private int quantity;

	private double price;

	@NotNull(message = "Category cannot be null.")
	private Long category;

	private Set<Long> writtenBy;

	public BookDTO() {
		super();
	}
}
