package com.ktsnwt.project.team9.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subscription_id")
	private Long id;

	@Column(unique = false, nullable = false)
	private Long dateOfPurchase;

	@Column(unique = false, nullable = false)
	private double price;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id")
	private RegisteredUser buyer;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "package_id")
	private Package purchasedPackage;
}
