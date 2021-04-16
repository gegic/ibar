package com.ktsnwt.project.team9.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentAuthorDTO {

	@NotNull(message = "Author cannot be null.")
	private Long author;

	private String text;
}