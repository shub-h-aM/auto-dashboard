package com.mll.automation.dashboard.reporting.exception;

import org.springframework.http.HttpStatus;

public class UserCustomExceptionWithStatusCode extends UserCustomException{
    private final HttpStatus httpStatus;
    public UserCustomExceptionWithStatusCode(HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }
    public UserCustomExceptionWithStatusCode(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
