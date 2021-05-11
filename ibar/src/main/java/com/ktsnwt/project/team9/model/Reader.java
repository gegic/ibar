package com.ktsnwt.project.team9.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reader extends User {

	private static final long serialVersionUID = 1L;
//
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(name = "reader_achievements",
//			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
//			inverseJoinColumns = @JoinColumn(name = "achievement_id", referencedColumnName = "achievement_id"))
//	private Set<Achievement> achievements;
}
