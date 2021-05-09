package com.ktsnwt.project.team9.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@OneToOne(targetEntity = Reader.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id")
	private Reader user;

	public VerificationToken(String token, Reader user) {
		this.token = token;
		this.user = user;
	}
}
