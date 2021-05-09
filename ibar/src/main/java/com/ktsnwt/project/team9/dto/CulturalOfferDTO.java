package com.ktsnwt.project.team9.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CulturalOfferDTO {

	@NotEmpty(message = "Name cannot be null or empty.")
	private String name;
	
	private String description;

	@Valid
	@NotNull(message = "Geolocation cannot be null.")
	private GeolocationDTO geolocation;
	
	@NotNull(message = "Category cannot be null.")
	private Long category;
	
	private Long admin;

	public CulturalOfferDTO() {
		super();
	}
	
	
}
