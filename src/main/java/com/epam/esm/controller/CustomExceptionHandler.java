package com.epam.esm.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception ex,
                                                    WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.NOT_FOUND);
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", 343);

        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
