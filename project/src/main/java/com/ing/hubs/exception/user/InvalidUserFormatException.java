package com.ing.hubs.exception.user;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidUserFormatException extends CustomException {
    public InvalidUserFormatException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Invalid format for this user! Check for the groupName if the user has student role.");
    }
}
