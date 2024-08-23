package com.ing.hubs.exception.course;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CourseIsAlreadyOnGoing extends CustomException {
    public CourseIsAlreadyOnGoing() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Course is already ongoing!");
    }
}
