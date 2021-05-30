package com.sbnz.ibar.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
@NoArgsConstructor
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(long id, String email, String password, String firstName, String lastName, long lastPasswordResetDate, ArrayList<Authority> authorities, boolean enabled) {
        super(id, email, password, firstName, lastName, lastPasswordResetDate, authorities, enabled);
    }
}
