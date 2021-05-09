package com.ktsnwt.project.team9.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("RU")
public class RegisteredUser extends User {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "title_id")
	private Title title;

	@OneToMany(mappedBy = "grader", cascade = CascadeType.ALL)
	private Set<MarkAuthor> marksAuthor;

	@OneToMany(mappedBy = "grader", cascade = CascadeType.ALL)
	private Set<MarkBook> marksBook;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private Set<CommentAuthor> commentsAuthor;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
	private Set<CommentBook> commentsBook;

	@ManyToMany
	@JoinTable(name = "read_book", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
	Set<Book> readBooks;

	@Column
	private boolean verified;

	public RegisteredUser(Long id) {
		super(id);
	}

	public RegisteredUser(Long id, String username, String email, String password, String firstName, String lastName,
			Set<MarkAuthor> marksAuthor, Set<MarkBook> marksBook, Set<CommentAuthor> commentsAuthor,
			Set<CommentBook> commentsBook, boolean verified) {
		super(id, username, email, password, firstName, lastName);
		this.marksAuthor = marksAuthor;
		this.marksBook = marksBook;
		this.commentsAuthor = commentsAuthor;
		this.commentsBook = commentsBook;
		this.verified = verified;
	}

	public RegisteredUser(String username, String email, String password, String firstName, String lastName) {
		super(username, email, password, firstName, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegisteredUser other = (RegisteredUser) obj;
		if (commentsAuthor == null) {
			if (other.commentsAuthor != null)
				return false;
		} else if (!commentsAuthor.equals(other.commentsAuthor))
			return false;
		if (commentsBook == null) {
			if (other.commentsBook != null)
				return false;
		} else if (!commentsBook.equals(other.commentsBook))
			return false;
		if (marksAuthor == null) {
			if (other.marksAuthor != null)
				return false;
		} else if (!marksAuthor.equals(other.marksAuthor))
			return false;
		if (marksBook == null) {
			if (other.marksBook != null)
				return false;
		} else if (!marksBook.equals(other.marksBook))
			return false;
		if (verified != other.verified)
			return false;
		return true;
	}

}
