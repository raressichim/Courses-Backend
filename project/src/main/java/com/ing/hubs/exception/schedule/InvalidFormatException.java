package com.ing.hubs.exception.schedule;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidFormatException extends CustomException {
    public InvalidFormatException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Invalid hours set! Start time should be before end time!");
    }
}
