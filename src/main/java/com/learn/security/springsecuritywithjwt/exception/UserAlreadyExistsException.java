package com.learn.security.springsecuritywithjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExistsException extends ResponseStatusException {
    public UserAlreadyExistsException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

}
