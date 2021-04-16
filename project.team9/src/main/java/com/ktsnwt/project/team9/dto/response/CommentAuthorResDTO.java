package com.ktsnwt.project.team9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentAuthorResDTO {

	private long id;

	private long dateTime;

	private String authorUsername;

	private String author;

	private String text;
}
