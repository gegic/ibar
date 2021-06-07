package com.sbnz.ibar.rto.events;

import com.sbnz.ibar.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Expires("72h")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnReview {
    private Review review;
}
