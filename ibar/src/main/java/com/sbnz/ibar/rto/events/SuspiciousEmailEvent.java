package com.sbnz.ibar.rto.events;

import com.sbnz.ibar.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

@Data
@Role(Role.Type.EVENT)
@Expires("2m")
@AllArgsConstructor
public class SuspiciousEmailEvent {
    private String email;
}
