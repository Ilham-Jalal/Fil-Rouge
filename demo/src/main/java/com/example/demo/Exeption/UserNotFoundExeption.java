package com.example.demo.Exeption;

public class UserNotFoundExeption extends RuntimeException{
    public UserNotFoundExeption(String id){super("user not found"+id);}
}
