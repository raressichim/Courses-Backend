package com.ing.hubs.exception.user;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends CustomException {
    public DuplicateEmailException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Duplicate email!");
    }
}
