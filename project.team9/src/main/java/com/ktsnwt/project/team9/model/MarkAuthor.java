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
@DiscriminatorValue("MA")
public class MarkAuthor extends Mark {

	private static final long serialVersionUID = 2407996498331315237L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "author_id")
	private Author author;

	public MarkAuthor(Long id, int value, RegisteredUser user, Author author) {
		super(id, value, user);
		this.author = author;
	}
}
