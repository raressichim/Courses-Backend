package com.ing.hubs.exception.schedule;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidEnumException extends CustomException {
    public InvalidEnumException() {
        super(HttpStatus.BAD_REQUEST, "Invalid enum value, check name and weekDay fields!");
    }
}
