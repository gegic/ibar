package com.sbnz.ibar.rto.events;

import com.sbnz.ibar.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Expires("7d")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnSubscribed {
    private User user;
}
