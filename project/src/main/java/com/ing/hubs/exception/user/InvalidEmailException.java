package com.ing.hubs.exception.user;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidEmailException extends CustomException {
    public InvalidEmailException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Invalid email");
    }
}
