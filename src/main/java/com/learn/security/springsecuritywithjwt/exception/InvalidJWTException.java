package com.learn.security.springsecuritywithjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidJWTException extends ResponseStatusException {
    public InvalidJWTException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
