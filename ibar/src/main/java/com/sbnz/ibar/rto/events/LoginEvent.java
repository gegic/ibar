package com.sbnz.ibar.rto.events;

import com.sbnz.ibar.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.util.Date;

@Role(Role.Type.EVENT)
@Timestamp("executionTime")
@Expires("5m")
@Data
@AllArgsConstructor
public class LoginEvent {
    private Date executionTime;
    private User user;
    private String ipAddress;
    private boolean success;
}
