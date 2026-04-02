package com.example.geographical.controller;

import com.example.geographical.model.User;
import com.example.geographical.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // DELETE USER
    @PutMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteUser(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(0); // 1 = ACTIVE, 0 = INACTIVE
        userRepository.save(user);

        return ResponseEntity.ok("User deactivated");
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            // Update fields
            user.setUserName(updatedUser.getUserName());
            user.setRole(updatedUser.getRole());

            User savedUser = userRepository.save(user);

            return ResponseEntity.ok(savedUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }}