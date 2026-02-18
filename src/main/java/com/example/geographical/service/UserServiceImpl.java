package com.example.geographical.service;
import com.example.geographical.dto.request.LoginRequestDTO;
import com.example.geographical.dto.request.SignupRequestDTO;
import com.example.geographical.model.User;
import com.example.geographical.repository.UserRepository;
import com.example.geographical.response.ResponseModel;
import com.example.geographical.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseModel signup(SignupRequestDTO request) {

        User user = new User()
                .setUserName(request.getUserName())
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
                .setCreatedDate(new Date());

        userRepository.save(user);

        return new ResponseModel()
                .setStatus(200)
                .setMessage("User registered successfully")
                .setData(user);
    }

    @Override
    public ResponseModel login(LoginRequestDTO request) {

        Optional<User> user =
                userRepository.findByUserNameAndPassword(
                        request.getUserName(),
                        request.getPassword());

        if (user.isPresent()) {
            return new ResponseModel()
                    .setStatus(200)
                    .setMessage("Login successful")
                    .setData(user.get());
        }

        return new ResponseModel()
                .setStatus(401)
                .setMessage("Invalid username or password")
                .setData(null);
    }
}
