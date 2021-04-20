package com.ktsnwt.project.team9.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ktsnwt.project.team9.enums.BookType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Package {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "package_id")
	private Long id;

	@Column(unique = false, nullable = false)
	private String name;

	@Column(unique = false, nullable = false)
	private double price;

	@Column(unique = false, nullable = false)
	private Long duration;

	@Column(unique = false, nullable = false)
	private BookType bookType;

	@Column(unique = false, nullable = false)
	private Set<Category> categories;
}
