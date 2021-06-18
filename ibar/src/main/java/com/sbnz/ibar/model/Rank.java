package com.sbnz.ibar.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.kie.api.definition.type.Position;

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
	@Column(nullable = false)
	private String name;

	@NonNull
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	private Long points;

	@OneToOne
	@EqualsAndHashCode.Exclude
	private Rank higherRank;

}