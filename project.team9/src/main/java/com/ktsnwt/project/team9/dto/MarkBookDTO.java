package com.ktsnwt.project.team9.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarkBookDTO {

	private Long id;

	@NotNull(message = "Mark cannot be null.")
	@Max(value = 5, message = "Mark should not be greater than 5.")
	@Min(value = 1, message = "Mark should not be lower than 1.")
	private int value;

	@NotNull(message = "Book cannot be null.")
	private Long book;

	public MarkBookDTO(int value, Long id) {
		this.value = value;
		this.book = id;
	}

}
