package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

	private UUID id;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	private String description;

	@NotNull(message = "Date of birth cannot be null.")
	private Date dateOfBirth;

	private Date dateOfDeath;

	private double averageRating;

	private String image;
}
