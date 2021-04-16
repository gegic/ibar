package com.ktsnwt.project.team9.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "author_id")
	private Long id;

	@Column(unique = false, nullable = false)
	private String name;

	@Column(unique = false, nullable = false)
	private String description;

	@Column(unique = false, nullable = true)
	private Long dateOfBirth;

	@Column(unique = false, nullable = true)
	private Long dateOfDeath;

	@Column(unique = false, nullable = false)
	private double averageMark;

	@ManyToMany
	@JoinTable(name = "written_books", joinColumns = @JoinColumn(name = "author_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
	Set<Book> writtenBooks;

	public Author(Long id) {
		this.id = id;
	}
}
