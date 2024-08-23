package com.ing.hubs.exception.user;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends CustomException {
    public InvalidCredentialsException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Invalid credentials! Check username or password!");
    }
}
