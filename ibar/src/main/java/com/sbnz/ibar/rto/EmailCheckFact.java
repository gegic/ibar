package com.sbnz.ibar.rto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailCheckFact {
    private String userEmail;
    private boolean isAllowed;
}
