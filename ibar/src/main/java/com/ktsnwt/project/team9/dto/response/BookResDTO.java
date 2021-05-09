package com.ktsnwt.project.team9.dto.response;

import java.util.Set;

import com.ktsnwt.project.team9.dto.CategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookResDTO {

	private Long id;

	private String name;

	private String description;

	private String image;

	private double averageMark;

	private int quantity;

	private double price;

	private CategoryDTO category;

	private Set<Long> writtenBy;

	Set<Long> boughtBy;

	public BookResDTO() {
		super();
	}

}
