package com.sbnz.ibar.rto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpCheckFact {
    private String ipAddress;
    private boolean isAllowed;
}
