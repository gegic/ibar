package com.sbnz.ibar.rto.events;

import com.sbnz.ibar.model.Plan;
import com.sbnz.ibar.model.Reader;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.time.Instant;

@Role(Role.Type.EVENT)
@Expires("7d")
@Timestamp("timestamp")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OnSubscribed {
    @NonNull
    private Reader user;
    @NonNull
    private Plan plan;
    private Long timestamp = Instant.now().toEpochMilli();
}
