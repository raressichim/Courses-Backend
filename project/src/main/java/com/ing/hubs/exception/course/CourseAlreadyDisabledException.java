package com.ing.hubs.exception.course;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CourseAlreadyDisabledException extends CustomException {
    public CourseAlreadyDisabledException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Course is already disabled");
    }
}
