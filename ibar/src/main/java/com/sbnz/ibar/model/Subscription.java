package com.sbnz.ibar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(
    		name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	private UUID id;

	@Column(nullable = false)
	private Instant dateOfPurchase;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	private Reader buyer;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	private Plan purchasedPlan;
}
