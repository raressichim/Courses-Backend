package com.ing.hubs.exception.user;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateUsernameException extends CustomException {
    public DuplicateUsernameException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Duplicate username!");
    }
}
