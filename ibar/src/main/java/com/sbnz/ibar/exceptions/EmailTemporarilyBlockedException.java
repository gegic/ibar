package com.sbnz.ibar.exceptions;

import org.springframework.security.core.AuthenticationException;

public class EmailTemporarilyBlockedException extends AuthenticationException {
    public EmailTemporarilyBlockedException(String msg) {
        super(msg);
    }
}
