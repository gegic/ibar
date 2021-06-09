package com.sbnz.ibar.model;

import com.sbnz.ibar.dto.UserDto;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(UUID id, String email, String password, String firstName, String lastName, long lastPasswordResetDate, ArrayList<Authority> authorities, boolean enabled) {
        super(id, email, password, firstName, lastName, lastPasswordResetDate, authorities, enabled);
    }

    public Admin(UserDto userDto) {
        this.setEmail(userDto.getEmail());

        this.setFirstName(userDto.getFirstName());
        this.setLastName(userDto.getLastName());

        this.setEnabled(true);
    }
}
