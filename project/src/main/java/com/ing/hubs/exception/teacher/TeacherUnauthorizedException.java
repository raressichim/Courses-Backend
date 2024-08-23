package com.ing.hubs.exception.teacher;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TeacherUnauthorizedException extends CustomException {
    public TeacherUnauthorizedException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("This teacher isn't the one that owns the course!");
    }
}
