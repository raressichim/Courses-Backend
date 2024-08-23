package com.ing.hubs.exception.course;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CourseFullException extends CustomException {
    public CourseFullException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Course is full");
    }
}
