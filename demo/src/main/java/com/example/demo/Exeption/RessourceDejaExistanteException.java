package com.example.demo.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RessourceDejaExistanteException extends RuntimeException {
    public RessourceDejaExistanteException(String message) {
        super(message);
    }
}
