package com.ing.hubs.exception.teacher;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidGradeException extends CustomException {
    public InvalidGradeException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("The grade should be in the interval [1,10]!");
    }
}
