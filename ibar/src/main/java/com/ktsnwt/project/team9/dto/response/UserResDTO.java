package com.ktsnwt.project.team9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResDTO {

	private Long id;
	
	private String username;
	
	private String email;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
}
