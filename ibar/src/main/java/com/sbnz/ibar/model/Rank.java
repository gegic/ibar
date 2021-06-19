package com.sbnz.ibar.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Rank {

	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(
    		name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	private UUID id;

	@NonNull
	@Column(nullable = false, unique = true)
	private String name;

	@NonNull
	@Column(nullable = false, unique = true)
	@EqualsAndHashCode.Exclude
	private Long points;

	@OneToOne
	@EqualsAndHashCode.Exclude
	private Rank higherRank;

}
