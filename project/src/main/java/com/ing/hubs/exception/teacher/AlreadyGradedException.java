package com.ing.hubs.exception.teacher;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AlreadyGradedException extends CustomException {
    public AlreadyGradedException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("This course has already been graded!");
    }
}
