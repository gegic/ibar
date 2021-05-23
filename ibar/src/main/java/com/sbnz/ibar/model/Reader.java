package com.sbnz.ibar.model;

import com.sbnz.ibar.utils.Utils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reader extends User {

	private static final long serialVersionUID = 1L;

	@Min(13)
	@Max(120)
	private long age;

	private boolean male;

	public String getCategory() {
		return Utils.getCategory(this);
	}

//
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(name = "reader_achievements",
//			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
//			inverseJoinColumns = @JoinColumn(name = "achievement_id", referencedColumnName = "achievement_id"))
//	private Set<Achievement> achievements;
}
