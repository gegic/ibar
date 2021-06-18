package com.sbnz.ibar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(
    		name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private double averageRating;

	private long numReviews;

	private UUID cover;

	private UUID pdf;

	// represents pages if an ebook, seconds if an audio book
	private long quantity;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Author> authors;

}
