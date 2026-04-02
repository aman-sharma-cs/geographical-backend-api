package com.example.geographical.controller;

import com.example.geographical.dto.request.LoginRequestDTO;
import com.example.geographical.dto.request.SignupRequestDTO;
import com.example.geographical.model.User;
import com.example.geographical.repository.UserRepository;
import com.example.geographical.response.ResponseModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // allow frontend calls
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // USER SIGNUP
    // =========================
    @PostMapping("/user/signup")
    public ResponseEntity<ResponseModel> userSignup(
            @RequestBody SignupRequestDTO request) {

        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole("USER"); // default role
        user.setStatus(1);
        user.setCreatedDate(String.valueOf(new Date()));

        userRepository.save(user);

        return ResponseEntity.ok(
                new ResponseModel()
                        .setStatus(200)
                        .setMessage("User registered successfully")
                        .setData(user)
        );
    }

    // =========================
    // ADMIN SIGNUP
    // =========================
    @PostMapping("/admin/signup")
    public ResponseEntity<ResponseModel> adminSignup(
            @RequestBody SignupRequestDTO request) {

        User admin = new User();
        admin.setUserName(request.getUserName());
        admin.setEmail(request.getEmail());
        admin.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        admin.setRole("ADMIN"); // admin role
        admin.setStatus(1);
        admin.setCreatedDate(String.valueOf(new Date()));

        userRepository.save(admin);

        return ResponseEntity.ok(
                new ResponseModel()
                        .setStatus(200)
                        .setMessage("Admin registered successfully")
                        .setData(admin)
        );
    }

    // =========================
    // LOGIN (USER + ADMIN)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<ResponseModel> login(
            @RequestBody LoginRequestDTO request) {

        Optional<User> optionalUser =
                userRepository.findByUserName(request.getUserName());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.ok(
                    new ResponseModel()
                            .setStatus(401)
                            .setMessage("Invalid username or password")
                            .setData(null)
            );
        }

        User user = optionalUser.get();

        // password check using encoder
        boolean passwordMatch =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatch) {
            return ResponseEntity.ok(
                    new ResponseModel()
                            .setStatus(401)
                            .setMessage("Invalid username or password")
                            .setData(null)
            );
        }

        if (user.getStatus() == 5) {
            return ResponseEntity.ok(
                    new ResponseModel()
                            .setStatus(403)
                            .setMessage("User account inactive")
                            .setData(null)
            );
        }

        return ResponseEntity.ok(
                new ResponseModel()
                        .setStatus(200)
                        .setMessage("Login successful")
                        .setData(user)
        );
    }
}