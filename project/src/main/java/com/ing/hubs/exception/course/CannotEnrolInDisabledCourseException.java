package com.ing.hubs.exception.course;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CannotEnrolInDisabledCourseException extends CustomException {
    public CannotEnrolInDisabledCourseException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("You can't enrol in a disabled course!");
    }
}
