package com.sbnz.ibar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
