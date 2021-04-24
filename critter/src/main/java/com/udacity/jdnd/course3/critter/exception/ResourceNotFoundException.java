package com.udacity.jdnd.course3.critter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// reason overrides any custom messages, so it'll be put in the default constructor to prevent that issue
@ResponseStatus(code = HttpStatus.NOT_FOUND)//, reason = "Resource not found"
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {
        super("**Resource not found**");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}