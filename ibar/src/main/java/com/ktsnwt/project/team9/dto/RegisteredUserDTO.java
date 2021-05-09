package com.ktsnwt.project.team9.dto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisteredUserDTO extends UserDTO {

	private Set<Long> marksAuthor;
	private Set<Long> marksBook;
	private Set<Long> commentsAuthor;
	private Set<Long> commentsBook;

	public RegisteredUserDTO(String username, String email, String password, String firstName, String lastName,
			Set<Long> marksAuthor, Set<Long> marksBook, Set<Long> commentsAuthor, Set<Long> commentsBook) {
		super(username, email, password, firstName, lastName);
		this.marksAuthor = marksAuthor;
		this.marksBook = marksBook;
		this.commentsAuthor = commentsAuthor;
		this.commentsBook = commentsBook;
	}
}
