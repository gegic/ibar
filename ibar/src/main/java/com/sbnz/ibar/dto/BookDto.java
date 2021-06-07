package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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
