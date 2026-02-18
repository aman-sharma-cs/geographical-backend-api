package com.example.geographical.controller;

import com.example.geographical.dto.request.LoginRequestDTO;
import com.example.geographical.dto.request.SignupRequestDTO;
import com.example.geographical.response.ResponseModel;
import com.example.geographical.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // SIGNUP API
    @PostMapping("/signup")
    public ResponseModel signup(
            @RequestBody SignupRequestDTO request) {
        System.out.println("Signup API hit");

        return userService.signup(request);
    }

    // LOGIN API
    @PostMapping("/login")
    public ResponseModel login(
            @RequestBody LoginRequestDTO request) {

        return userService.login(request);
    }
}

