package com.ktsnwt.project.team9.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("CB")
public class CommentBook extends Comment {

	private static final long serialVersionUID = -4153567747336303630L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "book_id")
	private Book book;

	public CommentBook(RegisteredUser registeredUser, Book book2, String text, long time) {
		super(registeredUser, text, time);
		this.book = book2;
	}
}
