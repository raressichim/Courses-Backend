package com.ing.hubs.exception.enrolment;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EnrolmentNotFoundException extends CustomException {
    public EnrolmentNotFoundException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Enrolment doesn't exist!");
    }
}
