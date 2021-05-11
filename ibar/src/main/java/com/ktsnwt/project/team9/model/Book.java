package com.ktsnwt.project.team9.model;

import com.ktsnwt.project.team9.model.enums.BookType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Lob
	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private double averageRating;

	private String image;

	@Column(nullable = false)
	private BookType type;

	// represents pages if an ebook, seconds if an audio book
	private long quantity;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Author> authors;
}
