package com.ing.hubs.exception.enrolment;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EnrolmentAlreadyExistsException extends CustomException {
    public EnrolmentAlreadyExistsException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Already requested to enroll to this course!");
    }
}
