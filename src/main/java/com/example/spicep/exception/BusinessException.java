package com.example.spicep.exception;

import com.example.spicep.model.model.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BusinessException extends ResponseStatusException {

    private final ErrorCode errorCode;

    public BusinessException(HttpStatusCode status, ErrorCode errorCode) {
        super(status);
        this.errorCode = errorCode;
    }
}
