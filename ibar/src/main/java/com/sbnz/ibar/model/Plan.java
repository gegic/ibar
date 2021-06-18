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
public class Plan {

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
	private double price;

	@ManyToMany
	private Set<Category> categories;

	@ManyToOne
	private Rank rank;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;

	@Column(nullable = false)
	private Long dayDuration;
}
