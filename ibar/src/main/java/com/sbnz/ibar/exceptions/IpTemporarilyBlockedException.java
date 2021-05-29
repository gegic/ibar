package com.sbnz.ibar.exceptions;

import org.springframework.security.core.AuthenticationException;

public class IpTemporarilyBlockedException extends AuthenticationException {
    public IpTemporarilyBlockedException(String msg) {
        super(msg);
    }
}
