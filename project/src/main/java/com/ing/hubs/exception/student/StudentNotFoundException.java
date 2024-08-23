package com.ing.hubs.exception.student;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class StudentNotFoundException extends CustomException {
    public StudentNotFoundException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Student not found!");
    }
}
