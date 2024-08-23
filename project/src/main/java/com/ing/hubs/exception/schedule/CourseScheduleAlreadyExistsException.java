package com.ing.hubs.exception.schedule;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CourseScheduleAlreadyExistsException extends CustomException {
    public CourseScheduleAlreadyExistsException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Course schedule already exists!");
    }
}
