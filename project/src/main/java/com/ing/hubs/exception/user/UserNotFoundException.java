package com.ing.hubs.exception.user;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(){
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("User not found");
    }
}
