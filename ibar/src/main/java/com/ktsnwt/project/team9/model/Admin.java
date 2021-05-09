package com.ktsnwt.project.team9.model;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Admin extends User {
	private static final long serialVersionUID = 1L;

	@Id
	@Override
	public Long getId() {
		return super.getId();
	}
}
