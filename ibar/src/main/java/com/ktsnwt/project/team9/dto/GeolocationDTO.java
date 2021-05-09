package com.ktsnwt.project.team9.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GeolocationDTO {

	private Long id;
	
	private String placeId;
	
	@NotEmpty(message = "Location cannot be null or empty.")
	private String location;
	
	@NotNull(message = "Lat cannot be null.")
	private double lat;
	
	@NotNull(message = "Lon cannot be null.")
	private double lon;

	public GeolocationDTO() {
		super();
	}

	public GeolocationDTO(String placeId, @NotEmpty(message = "Location cannot be null or empty.") String location,
			@NotNull(message = "Lat cannot be null.") double lat,
			@NotNull(message = "Lon cannot be null.") double lon) {
		super();
		this.placeId = placeId;
		this.location = location;
		this.lat = lat;
		this.lon = lon;
	}
	
}
