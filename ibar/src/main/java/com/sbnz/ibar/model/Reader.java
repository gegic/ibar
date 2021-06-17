package com.sbnz.ibar.model;

import com.sbnz.ibar.dto.UserDto;
import com.sbnz.ibar.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reader extends User {

    private static final long serialVersionUID = 1L;

    @Min(13)
    @Max(120)
    private long age;

    private boolean male;

    private double points = 0;

    @ManyToOne(optional = false)
    private Rank rank;

    public String getCategory() {
        return Utils.getCategory(this);
    }

    public Reader(UUID id, String email, String password, String firstName, String lastName, long lastPasswordResetDate, ArrayList<Authority> authorities, boolean enabled) {
        super(id, email, password, firstName, lastName, lastPasswordResetDate, authorities, enabled);
    }

    public Reader(UserDto userDto) {
        this.setEmail(userDto.getEmail());

        this.setFirstName(userDto.getFirstName());
        this.setLastName(userDto.getLastName());

        this.setAge(userDto.getAge());

        this.setMale(userDto.isMale());

        this.setEnabled(true);
    }
}
