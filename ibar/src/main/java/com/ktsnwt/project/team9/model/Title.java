package com.ktsnwt.project.team9.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Title {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "title_id")
	private Long id;

	@Column(unique = false, nullable = false)
	private String name;

}
