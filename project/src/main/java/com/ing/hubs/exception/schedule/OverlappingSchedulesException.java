package com.ing.hubs.exception.schedule;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OverlappingSchedulesException extends CustomException {
    public OverlappingSchedulesException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("This course overlaps with a course you are already enrolled in!");
    }
}
