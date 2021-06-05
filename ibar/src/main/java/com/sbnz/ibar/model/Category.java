package com.sbnz.ibar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Category {

	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(
    		name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	private UUID id;

	@Column(unique = true, nullable = false)
	private String name;

	private String description;

	@Column
	private boolean active;

}
