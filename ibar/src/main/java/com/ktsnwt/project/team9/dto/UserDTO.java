package com.ktsnwt.project.team9.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ktsnwt.project.team9.model.enums.UserType;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

	@NotEmpty(message = "Username cannot be null or empty.")
	private String username;
	
	@NotEmpty(message = "Email cannot be null or empty.")
	@Email(message = "Email format is not valid.")
	private String email;
	
	@NotEmpty(message = "First name cannot be null or empty.")
	private String firstName;
	
	@NotEmpty(message = "Last name cannot be null or empty.")
	private String lastName;

	@NotNull
	private UserType userType;
	
}
