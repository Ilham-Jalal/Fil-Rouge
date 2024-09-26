package com.example.demo.exceptions;

public class UserNotFoundExeption extends RuntimeException{
    public UserNotFoundExeption(String id){super("user not found"+id);}
}
