package com.ktsnwt.project.team9.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorDTO {

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	private String description;

	@NotNull(message = "Date of birth cannot be null.")
	private String dateOfBirth;

	private String dateOfDeath;

}
