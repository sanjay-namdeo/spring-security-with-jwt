package com.learn.security.springsecuritywithjwt.advice;

import com.learn.security.springsecuritywithjwt.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), ex.getMessage(), LocalDateTime.now());
        log.info("ErrorResponse built for error: " + errorResponse.getMessage());
        return ResponseEntity.status(status).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    @ExceptionHandler(value = {IncorrectUsernameOrPasswordException.class})
    public ResponseEntity<Object> handleUnAuthorizedException(IncorrectUsernameOrPasswordException exception) {
        return buildErrorResponse(exception, exception.getStatus());
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserAlreadyExist(UserAlreadyExistsException exception) {
        return buildErrorResponse(exception, exception.getStatus());
    }

    @ExceptionHandler(value = {InvalidJWTException.class})
    public ResponseEntity<Object> handleMalformedJwtException(InvalidJWTException exception) {
        return buildErrorResponse(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {ExpiredJWTException.class})
    public ResponseEntity<Object> handleExpiredJWTException(ExpiredJWTException exception) {
        return buildErrorResponse(exception, HttpStatus.FORBIDDEN);
    }
}
