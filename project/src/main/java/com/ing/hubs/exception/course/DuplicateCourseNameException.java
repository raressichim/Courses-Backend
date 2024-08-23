package com.ing.hubs.exception.course;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateCourseNameException extends CustomException {
    public DuplicateCourseNameException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("This course already exists!");
    }
}
