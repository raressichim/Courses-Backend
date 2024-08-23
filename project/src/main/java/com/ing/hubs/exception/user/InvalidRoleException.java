package com.ing.hubs.exception.user;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidRoleException extends CustomException {
    public InvalidRoleException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("The role should be student or teacher!");
    }
}
