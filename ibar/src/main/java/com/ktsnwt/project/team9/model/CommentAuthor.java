package com.ktsnwt.project.team9.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CA")
public class CommentAuthor extends Comment {

	private static final long serialVersionUID = -6349434426623484859L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "author_id")
	private Author author;

	public CommentAuthor(RegisteredUser registeredUser, Author author, String text, long time) {
		super(registeredUser, text, time);
		this.author = author;
	}
}
