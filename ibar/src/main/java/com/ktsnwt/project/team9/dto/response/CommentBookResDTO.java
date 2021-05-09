package com.ktsnwt.project.team9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentBookResDTO {

	private long id;

	private long dateTime;

	private String authorUsername;

	private String book;

	private String text;
}
