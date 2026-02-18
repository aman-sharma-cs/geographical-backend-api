package com.example.geographical.service;

import com.example.geographical.dto.request.LoginRequestDTO;
import com.example.geographical.dto.request.SignupRequestDTO;
import com.example.geographical.response.ResponseModel;

public interface UserService {
    ResponseModel signup(SignupRequestDTO request);

    ResponseModel login(LoginRequestDTO request);
}
