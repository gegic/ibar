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
@DiscriminatorValue("MB")
public class MarkBook extends Mark {

	private static final long serialVersionUID = -1012483313236368205L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "book_id")
	private Book book;

	public MarkBook(Long id, int value, RegisteredUser user, Book book) {
		super(id, value, user);
		this.book = book;
	}
}
