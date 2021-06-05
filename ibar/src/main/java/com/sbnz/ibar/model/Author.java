package com.sbnz.ibar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Author {

	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(
    		name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	private Instant dateOfBirth;

	private Instant dateOfDeath;

	@Column(nullable = false)
	private double averageRating;

	private String image;
}
