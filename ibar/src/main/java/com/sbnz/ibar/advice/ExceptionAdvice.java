package com.sbnz.ibar.advice;

import com.sbnz.ibar.dto.ErrorDto;
import com.sbnz.ibar.exceptions.EmailTemporarilyBlockedException;
import com.sbnz.ibar.exceptions.IpTemporarilyBlockedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IpTemporarilyBlockedException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> onIpTemporarilyBlockedException(IpTemporarilyBlockedException exception) {
        return ResponseEntity.badRequest().body(new ErrorDto("Your IP address was temporarily blocked",
                exception.getMessage()));
    }

    @ExceptionHandler(EmailTemporarilyBlockedException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> onEmailTemporarilyBlockedException(EmailTemporarilyBlockedException exception) {
        return ResponseEntity.badRequest().body(new ErrorDto("Your account was temporarily blocked",
                exception.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> onAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.badRequest().body(new ErrorDto("Bad credentials", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Void> onException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


}
