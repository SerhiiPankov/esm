package com.epam.esm.controller;

import com.epam.esm.exception.DataProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = DataProcessingException.class)
    protected ResponseEntity<Object> handleConflict(DataProcessingException ex,
                                                    WebRequest request) {
        return handleExceptionInternal(ex, "WWW",
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
