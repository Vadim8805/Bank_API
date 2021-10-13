package com.example.bankapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormatNumberException extends RuntimeException {
    public FormatNumberException(String message) {
        super(message);
    }
}
