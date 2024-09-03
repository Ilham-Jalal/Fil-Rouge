package com.example.demo.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FichierNonTrouveException extends RuntimeException {
    public FichierNonTrouveException(String message) {
        super(message);
    }
}
