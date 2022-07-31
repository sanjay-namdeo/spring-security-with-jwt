package com.learn.security.springsecuritywithjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExpiredJWTException extends ResponseStatusException {
    public ExpiredJWTException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
