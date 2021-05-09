package com.ktsnwt.project.team9.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.*;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	private String description;

	@Column
	private boolean active;

	@ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
	private Set<Plan> plans;

}
