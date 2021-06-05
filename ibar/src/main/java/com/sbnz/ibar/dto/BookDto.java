package com.sbnz.ibar.dto;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	private UUID id;
	
	@NotEmpty(message = "Name cannot be null or empty.")
	private String name;

	private String description;

	private long quantity;

	@NotNull(message = "Category cannot be null.")
	private UUID categoryId;

	private String categoryName;
	private String authorName;
	private String cover;
	private Long numReviews;
	private Double averageRating;
	private boolean inReadingList;
	private Long numRead;
	private String pdf;

}
