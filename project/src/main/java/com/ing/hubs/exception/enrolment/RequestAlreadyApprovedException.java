package com.ing.hubs.exception.enrolment;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class RequestAlreadyApprovedException extends CustomException {
    public RequestAlreadyApprovedException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("This request is already approved");
    }
}
