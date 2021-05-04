package com.ktsnwt.project.team9.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ktsnwt.project.team9.enums.BookType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private Long id;

	@Column(unique = false, nullable = false)
	private String name;

	@Column(unique = false, nullable = false)
	@Lob
	private String description;

	@Column(unique = false, nullable = false)
	private int quantity;

	@Column(unique = false, nullable = false)
	private double price;

	@Column(unique = false, nullable = false)
	private double averageMark;

	@Column(unique = false, nullable = true)
	private String image;

	@Column(unique = false, nullable = false)
	private BookType type;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToMany(mappedBy = "writtenBooks", fetch = FetchType.LAZY)
	private Set<Author> writtenBy;

	@ManyToMany(mappedBy = "readBooks")
	private Set<RegisteredUser> readBy;

	@OneToMany(mappedBy = "book")
	private Set<CommentBook> comments;

	public Book(Long id) {
		this.id = id;
	}

	public Book(String name, String description, Category category, double price, int quantity, Set<Author> authors) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.writtenBy = authors;
	}
}
