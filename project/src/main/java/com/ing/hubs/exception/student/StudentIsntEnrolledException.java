package com.ing.hubs.exception.student;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class StudentIsntEnrolledException extends CustomException {
    public StudentIsntEnrolledException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Student isn't enrolled in this course!");
    }
}
