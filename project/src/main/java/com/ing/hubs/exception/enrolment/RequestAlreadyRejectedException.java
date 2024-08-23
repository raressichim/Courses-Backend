package com.ing.hubs.exception.enrolment;

import com.ing.hubs.exception.CustomException;
import org.springframework.http.HttpStatus;

public class RequestAlreadyRejectedException extends CustomException {
    public RequestAlreadyRejectedException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("This request has already been rejected");
    }
}
