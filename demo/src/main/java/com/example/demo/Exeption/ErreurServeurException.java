package com.example.demo.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErreurServeurException extends RuntimeException {
    public ErreurServeurException(String message) {
        super(message);
    }
}
