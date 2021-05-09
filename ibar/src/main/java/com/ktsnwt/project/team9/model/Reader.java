package com.ktsnwt.project.team9.model;

import java.util.Set;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reader extends User {

	private static final long serialVersionUID = 1L;

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Achievement> achievements;

	@Id
	@Override
	public Long getId() {
		return super.getId();
	}

}
