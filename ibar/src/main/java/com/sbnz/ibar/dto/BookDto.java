package com.sbnz.ibar.dto;

import com.sbnz.ibar.model.enums.BookType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
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

	private BookType type;

	private Set<UUID> authorIds;
	private Set<String> authorNames;
	private String categoryName;
	private UUID cover;
	private Long numReviews;
	private Double averageRating;
	private boolean inReadingList;
	private Long numRead;
	private UUID pdf;

}
