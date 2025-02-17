package com.example.spicep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WalletAlreadyExistsException extends ResponseStatusException {

    public WalletAlreadyExistsException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
