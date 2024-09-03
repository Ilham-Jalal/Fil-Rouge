package com.example.demo.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccesNonAutoriseException extends RuntimeException {
    public AccesNonAutoriseException(String message) {
        super(message);
    }
}
