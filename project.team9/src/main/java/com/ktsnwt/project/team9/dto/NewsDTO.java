package com.ktsnwt.project.team9.dto;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

	private Long ID;
	
	@NotBlank(message = "News content cannot be empty.")
	private String content;
	
	@Min(value = 0L, message = "The value must be positive")
	@NotNull(message = "News date cannot be empty.")
	private long date;

	@NotBlank(message = "News title cannot be empty.")
	private String title;
	
	private boolean active;

	private Set<String> images;
	
	@NotNull(message = "ID  of CulturalOffer cannot be null or empty.")
	private Long culturalOfferID;
	
}
