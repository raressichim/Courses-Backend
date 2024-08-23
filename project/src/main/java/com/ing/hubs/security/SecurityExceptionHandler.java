package com.ing.hubs.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(Exception exception) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status;

        exception.printStackTrace();

        if (exception instanceof InternalAuthenticationServiceException || exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            response.put("message", "The username or password is incorrect");
        } else if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            response.put("message", "The account is locked");
        } else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            response.put("message", "You are not authorized to access this resource");
        } else if (exception instanceof SignatureException) {
            status = HttpStatus.UNAUTHORIZED;
            response.put("message", "The JWT signature is invalid");
        } else if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED;
            response.put("message", "JWT expired");
        } else if (exception instanceof MalformedJwtException) {
            status = HttpStatus.UNAUTHORIZED;
            response.put("message", "Malformed JWT");
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            response.put("message", "Unknown error");
        }


        return new ResponseEntity<>(response, status);
    }
}
