package com.musala.dronedelivery.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AppException extends Exception {

    @Getter
    private final HttpStatus status;

    public AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}

