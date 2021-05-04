package com.ktsnwt.project.team9.dto.response;

import java.util.Date;
import java.util.Set;

import com.ktsnwt.project.team9.model.CommentAuthor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorResDTO {

	private Long id;

	private String name;

	private String description;

	private String image;

	private double averageMark;

	private String dateOfBirth;

	private String dateOfDeath;

	private Set<Long> writtenBooks;

	private Set<CommentAuthor> comments;
}
