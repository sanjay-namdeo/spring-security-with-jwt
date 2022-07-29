package com.learn.security.springsecuritywithjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IncorrectUsernameOrPasswordException extends ResponseStatusException {
    public IncorrectUsernameOrPasswordException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
