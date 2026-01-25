package com.chat.auth.services;

import com.chat.auth.DTOs.LoginRequest;
import com.chat.auth.DTOs.LoginResponse;
import com.chat.auth.DTOs.SignUpRequest;
import com.chat.auth.DTOs.UserResponse;

public interface AuthService {
    UserResponse signUp(SignUpRequest request);
    LoginResponse login(LoginRequest request);
}
