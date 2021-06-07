package com.sbnz.ibar.rto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

@Data
@Role(Role.Type.EVENT)
@Expires("2m")
@AllArgsConstructor
public class OnSuspiciousIp {
    private String ipAddress;
}
