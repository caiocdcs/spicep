package com.example.spicep.exception;

import com.example.spicep.model.APIErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { BusinessException.class })
    protected ResponseEntity<APIErrorResponse> handleResponseStatusException(BusinessException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new APIErrorResponse(e.getErrorCode()));
    }
}
