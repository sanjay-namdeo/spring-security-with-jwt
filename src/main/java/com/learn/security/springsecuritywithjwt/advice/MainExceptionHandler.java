package com.learn.security.springsecuritywithjwt.advice;

import com.learn.security.springsecuritywithjwt.exception.ErrorResponse;
import com.learn.security.springsecuritywithjwt.exception.IncorrectUsernameOrPasswordException;
import com.learn.security.springsecuritywithjwt.exception.InvalidJWTException;
import com.learn.security.springsecuritywithjwt.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class MainExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(UserNotFoundException exception) {
        log.info("User is not available!");
        return buildErrorResponse(exception, exception.getStatus());
    }

    @ExceptionHandler(IncorrectUsernameOrPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleIncorrectUserOrPassword(IncorrectUsernameOrPasswordException exception) {
        log.info("Username or password is wrong!");
        return buildErrorResponse(exception, exception.getStatus());
    }

    public ResponseEntity<Object> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), ex.getMessage(), LocalDateTime.now());
        log.info("ErrorResponse built for error: " + errorResponse.getMessage());
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(InvalidJWTException.class)
    public ResponseEntity<Object> handleInvalidJWTToken(InvalidJWTException exception) {
        log.error("Invalid JWT token");
        return buildErrorResponse(exception, exception.getStatus());
    }
}
