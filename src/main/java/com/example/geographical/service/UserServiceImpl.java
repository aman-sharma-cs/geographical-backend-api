package com.example.geographical.service;

import com.example.geographical.dto.request.LoginRequestDTO;
import com.example.geographical.dto.request.SignupRequestDTO;
import com.example.geographical.model.User;
import com.example.geographical.repository.UserRepository;
import com.example.geographical.response.ResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= SIGNUP =================
    @Override
    public ResponseModel signup(SignupRequestDTO request) {

        User user = new User();

        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setStatus(1);

        userRepository.save(user);

        return new ResponseModel()
                .setStatus(200)
                .setMessage("User registered successfully")
                .setData(user);
    }

    // ================= LOGIN =================
    @Override
    public ResponseModel login(LoginRequestDTO request) {

        Optional<User> optionalUser =
                userRepository.findByUserName(request.getUserName());

        if (optionalUser.isEmpty()) {
            return new ResponseModel()
                    .setStatus(401)
                    .setMessage("Invalid username or password");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            return new ResponseModel()
                    .setStatus(401)
                    .setMessage("Invalid username or password");
        }

        if (user.getStatus() == 5) {
            return new ResponseModel()
                    .setStatus(403)
                    .setMessage("User account is inactive");
        }

        return new ResponseModel()
                .setStatus(200)
                .setMessage("Login successful")
                .setData(user);
    }
}