package com.ing.hubs.exception.teacher;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TeacherNotFoundException extends CustomException {
    public TeacherNotFoundException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Teacher not found!");
    }
}
