package com.epam.esm.controller;

import com.epam.esm.exception.DataProcessingException;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.RepositoryReflectionException;
import com.epam.esm.exception.RequestException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = RepositoryReflectionException.class)
    protected ResponseEntity<Object> handleConflict(RepositoryReflectionException ex,
                                                    WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.NOT_FOUND);
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", ErrorCode.REFLECTION.getErrorCode());
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = RequestException.class)
    protected ResponseEntity<Object> handleConflict(RequestException ex,
                                                    WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", ErrorCode.REQUEST.getErrorCode());
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = DataProcessingException.class)
    protected ResponseEntity<Object> handleConflict(DataProcessingException ex,
                                                    WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.NOT_FOUND);
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", ErrorCode.DATA_PROCESSING.getErrorCode());
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
