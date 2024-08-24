package com.example.demo.controllers;

import com.example.demo.dto.SignUpRequest;
import com.example.demo.enums.Role;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            User user = userService.signUpUser(signUpRequest);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add/{role}")
    public ResponseEntity<User> addUserByAdmin(@PathVariable Role role, @RequestBody SignUpRequest signUpRequest) {
            User user = userService.addUserByAdmin(role, signUpRequest);
            return new ResponseEntity<>(user, HttpStatus.CREATED);

    }
}
