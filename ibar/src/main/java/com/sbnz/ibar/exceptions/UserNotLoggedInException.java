package com.sbnz.ibar.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotLoggedInException extends AuthenticationException {
    public UserNotLoggedInException(String msg) {
        super(msg);
    }
}
