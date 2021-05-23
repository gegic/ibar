package com.sbnz.ibar.advice;

import com.sbnz.ibar.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> onAuthenticationException(AuthenticationException exception){
        return ResponseEntity.badRequest().body(new ErrorDto("Bad credentials", exception.getMessage()));
    }

}
