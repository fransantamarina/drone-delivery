package com.musala.dronedelivery.controller;


import com.musala.dronedelivery.dto.ErrorDTO;
import com.musala.dronedelivery.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDTO> appExceptionHandler(AppException e) {
        var error = new ErrorDTO();
        error.setStatus(e.getStatus().getReasonPhrase());
        error.setError(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        var error = new ErrorDTO();
        error.setStatus(BAD_REQUEST.getReasonPhrase());
        error.setErrors(new ArrayList<>());

        if (e.getGlobalErrorCount() > 0) {
            var globalErrors = e.getGlobalErrors().stream().map(err -> {
                var globalError = new ErrorDTO.FieldError();
                globalError.setField(err.getObjectName());
                globalError.setMessage(err.getDefaultMessage());
                return globalError;
            }).toList();
            error.getErrors().addAll(globalErrors);
        }

        if (e.getFieldErrorCount() > 0) {
            var fieldErrors = e.getFieldErrors().stream().map(err -> {
                var fieldError = new ErrorDTO.FieldError();
                fieldError.setField(err.getField());
                fieldError.setMessage(err.getDefaultMessage());
                return fieldError;
            }).toList();
            error.getErrors().addAll(fieldErrors);
        }

        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDTO> noHandlerFoundExceptionHandler(NoHandlerFoundException e) {
        var error = new ErrorDTO();
        error.setStatus(NOT_FOUND.getReasonPhrase());
        error.setError("Resource not found '" + e.getRequestURL() + "'");
        return ResponseEntity.status(NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> exceptionHandler(Exception e) {
        var error = new ErrorDTO();
        error.setStatus(INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setError(e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

}

