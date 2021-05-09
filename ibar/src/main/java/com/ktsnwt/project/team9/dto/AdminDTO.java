package com.ktsnwt.project.team9.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
	
	@NotEmpty(message = "Username cannot be null or empty.")
	private String username;
	
	@NotEmpty(message = "Email cannot be null or empty.")
	@Email(message = "Email format is not valid.")
	private String email;
	
	@NotEmpty(message = "First name cannot be null or empty.")
	private String firstName;
	
	@NotEmpty(message = "Last name cannot be null or empty.")
	private String lastName;
}
